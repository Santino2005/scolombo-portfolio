import { useEffect, useState } from "react";
import {registerEntry, getTodayVisits, getVisitHistory, downloadVisitHistoryExcel} from "../api/visitApi";
import { createVisitor, findVisitorByDni, countVisitors } from "../api/visitorApi";

import Navbar from "../components/Navbar";
import AppShell from "../components/AppShell";
import CameraCapture from "../components/CameraCapture";
import CredentialModal from "../components/CredentialModal";
import ScannerModal from "../components/ScannerModal";

const sectors = [
    "Administración",
    "Operaciones",
    "Logística",
    "Almacén",
    "Seguridad",
    "Recepción",
    "Mantenimiento",
];

export default function GuardPage() {
    const [form, setForm] = useState({
        dni: "",
        fullName: "",
        company: "",
        sector: "Operaciones",
        photo: null,
        photoUrl: "",
    });
    const [visit, setVisit] = useState(null);
    const [scannerOpen, setScannerOpen] = useState(false);
    const [credentialOpen, setCredentialOpen] = useState(false);
    const [loading, setLoading] = useState(false);

    const [todayCount, setTodayCount] = useState(0);
    const [totalVisitors, setTotalVisitors] = useState(0);
    const [history, setHistory] = useState([]);

    useEffect(() => {
        loadDashboard();
    }, []);

    function updateField(field, value) {
        setForm({
            ...form,
            [field]: value,
        });
    }

    async function loadDashboard() {
        try {
            const today = await getTodayVisits();
            const historyData = await getVisitHistory();
            const total = await countVisitors();

            setTodayCount(today.data.length);
            setHistory(historyData.data);
            setTotalVisitors(total.data);
        } catch {
            console.log("Dashboard error");
        }
    }

    async function searchVisitor() {
        try {
            const response = await findVisitorByDni(form.dni);

            setForm({
                ...form,
                fullName: response.data.fullName,
                company: response.data.company,
                sector: response.data.sector || "Operaciones",
                photoUrl: response.data.photoUrl || "",
                photo: null,
            });
        } catch {
            alert("Visitante no encontrado");
        }
    }

    async function registerVisitor() {
        try {
            setLoading(true);

            await createVisitor(form);

            alert("Visitante registrado");

            await loadDashboard();
        } catch {
            alert("No se pudo registrar visitante");
        } finally {
            setLoading(false);
        }
    }

    async function generateCredential() {
        try {
            setLoading(true);

            const response = await registerEntry(
                form.dni,
                form.sector
            );

            setVisit(response.data);
            setCredentialOpen(true);

            await loadDashboard();
        } catch {
            alert("No se pudo generar credencial");
        } finally {
            setLoading(false);
        }
    }

    async function downloadExcel() {
        const response = await downloadVisitHistoryExcel();

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");

        link.href = url;
        link.setAttribute("download", "visit-history.xlsx");
        document.body.appendChild(link);
        link.click();

        link.remove();
        window.URL.revokeObjectURL(url);
    }

    function cleanForm() {
        setForm({
            dni: "",
            fullName: "",
            company: "",
            sector: "Operaciones",
            photo: null,
            photoUrl: "",
        });

        setVisit(null);
    }

    function closeScanner() {
        setScannerOpen(false);
        loadDashboard();
    }


    return (
        <AppShell>
            <Navbar />

            <section className="card">
                <h2 className="card-title">
                    Registro de Visitantes
                </h2>

                <div className="row">
                    <input
                        placeholder="DNI"
                        value={form.dni}
                        onChange={(e) =>
                            updateField("dni", e.target.value)
                        }
                    />

                    <button onClick={searchVisitor}>
                        🔍 Buscar
                    </button>
                </div>

                <input
                    placeholder="Nombre Completo"
                    value={form.fullName}
                    onChange={(e) =>
                        updateField("fullName", e.target.value)
                    }
                />

                <input
                    placeholder="Empresa"
                    value={form.company}
                    onChange={(e) =>
                        updateField("company", e.target.value)
                    }
                />

                <select
                    value={form.sector}
                    onChange={(e) =>
                        updateField("sector", e.target.value)
                    }
                >
                    {sectors.map((sector) => (
                        <option key={sector}>
                            {sector}
                        </option>
                    ))}
                </select>

                <div className="camera-label">
                    📷 FOTO DEL VISITANTE
                </div>

                <CameraCapture
                    onCapture={(photo) =>
                        updateField("photo", photo)
                    }
                />

                {form.photoUrl && !form.photo && (
                    <div className="last-photo-box">
                        <p>Última foto registrada</p>
                        <img
                            src={form.photoUrl}
                            alt="Última foto del visitante"
                            className="last-visitor-photo"
                        />
                    </div>
                )}

                <div className="actions">
                    <button
                        className="primary"
                        onClick={registerVisitor}
                    >
                        ✅ Registrar
                    </button>

                    <button onClick={cleanForm}>
                        🧹 Limpiar
                    </button>
                </div>

                <button
                    className="wide-button"
                    onClick={generateCredential}
                >
                    🪪 Generar credencial
                </button>

                {visit && (
                    <button
                        className="wide-button"
                        onClick={() =>
                            setCredentialOpen(true)
                        }
                    >
                        👁 Ver credencial
                    </button>
                )}

                <div className="divider" />

                <button
                    className="wide-button"
                    onClick={() => setScannerOpen(true)}
                >
                    📷 Escanear QR
                </button>
            </section>

            <section className="dashboard-section">
                <h3 className="dashboard-title">
                    Dashboard
                </h3>

                <div className="metrics">
                    <div className="metric">
                        <strong>{todayCount}</strong>
                        <span>INGRESOS HOY</span>
                    </div>

                    <div className="metric">
                        <strong>{totalVisitors}</strong>
                        <span>TOTAL REGISTRADOS</span>
                    </div>
                </div>

                <button className="wide-button" onClick={downloadExcel}>
                    📥 Descargar Excel
                </button>
                <div className="history-table-wrapper">
                    {history.length === 0 ? (
                        <div className="empty-history">
                            No hay registros todavía
                        </div>
                    ) : (
                        <table className="history-table">
                            <thead>
                            <tr>
                                <th>FECHA</th>
                                <th>INGRESO</th>
                                <th>SALIDA</th>
                                <th>DNI</th>
                                <th>NOMBRE</th>
                                <th>EMPRESA</th>
                                <th>SECTOR</th>
                                <th>ESTADO</th>
                            </tr>
                            </thead>

                            <tbody>
                            {history.slice(0, 8).map((item) => (
                                <tr key={item.id || item.qrToken}>
                                    <td>
                                        {item.entryTime
                                            ? new Date(item.entryTime).toLocaleDateString()
                                            : "-"}
                                    </td>

                                    <td>
                                        {item.entryTime
                                            ? new Date(item.entryTime).toLocaleTimeString([], {
                                                hour: "2-digit",
                                                minute: "2-digit",
                                            })
                                            : "-"}
                                    </td>

                                    <td>
                                        {item.exitTime
                                            ? new Date(item.exitTime).toLocaleTimeString([], {
                                                hour: "2-digit",
                                                minute: "2-digit",
                                            })
                                            : "-"}
                                    </td>

                                    <td>{item.visitor?.dni || "-"}</td>
                                    <td>{item.visitor?.fullName || "-"}</td>
                                    <td>{item.visitor?.company || "-"}</td>
                                    <td>{item.sector || "-"}</td>

                                    <td>
                            <span
                                className={
                                    item.exitTime
                                        ? "status-finished"
                                        : "status-active"
                                }
                            >
                                {item.exitTime ? "Finalizado" : "Activo"}
                            </span>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    )}
                </div>
            </section>

            {credentialOpen && visit && (
                <CredentialModal
                    visit={visit}
                    onClose={() =>
                        setCredentialOpen(false)
                    }
                />
            )}

            {scannerOpen && (
                <ScannerModal
                    onClose={closeScanner}
                />
            )}
        </AppShell>
    );
}