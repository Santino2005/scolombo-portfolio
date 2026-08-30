import { useEffect, useState, useMemo } from "react";
import { registerEntry, getTodayVisits, getVisitHistory, downloadVisitHistoryExcel } from "../api/visitApi";
import { createVisitor, findVisitorByDni, countVisitors } from "../api/visitorApi";
import { SECTORS } from "../constants/sectors";
import { APP_CONFIG } from "../constants/config";
import { formatTime, formatDate } from "../utils/dateFormatter";

import Navbar from "../components/Navbar";
import AppShell from "../components/AppShell";
import CameraCapture from "../components/CameraCapture";
import CredentialModal from "../components/CredentialModal";
import ScannerModal from "../components/ScannerModal";
import {
    IconCalendar,
    IconActivity,
    IconUsers,
    IconDownload,
    IconFileText,
    IconSearch,
    IconCheck,
    IconRefresh,
    IconIdCard,
    IconQrCode,
    IconTable,
} from "../components/Icons";

export default function GuardPage() {
    const [form, setForm] = useState({
        dni: "",
        fullName: "",
        company: "",
        sector: APP_CONFIG.DEFAULT_SECTOR,
        photo: null,
        photoUrl: "",
    });

    const [visit, setVisit] = useState(null);
    const [scannerOpen, setScannerOpen] = useState(false);
    const [credentialOpen, setCredentialOpen] = useState(false);
    const [loading, setLoading] = useState(false);
    const [feedback, setFeedback] = useState({ message: "", type: "" });

    const [todayCount, setTodayCount] = useState(0);
    const [totalVisitors, setTotalVisitors] = useState(0);
    const [history, setHistory] = useState([]);
    const [tableSearch, setTableSearch] = useState("");

    useEffect(() => {
        loadDashboard();
    }, []);

    function showFeedback(message, type = "success") {
        setFeedback({ message, type });
        setTimeout(() => setFeedback({ message: "", type: "" }), 4000);
    }

    function updateField(field, value) {
        setForm((prev) => ({
            ...prev,
            [field]: value,
        }));
    }

    async function loadDashboard() {
        try {
            const [todayRes, historyRes, totalRes] = await Promise.all([
                getTodayVisits(),
                getVisitHistory(),
                countVisitors(),
            ]);

            setTodayCount(todayRes.data?.length || 0);
            setHistory(historyRes.data || []);
            setTotalVisitors(totalRes.data || 0);
        } catch (err) {
            console.error("Error loading dashboard metrics:", err);
        }
    }

    async function searchVisitor() {
        if (!form.dni.trim()) {
            showFeedback("Ingrese un DNI para buscar", "error");
            return;
        }

        try {
            setLoading(true);
            const response = await findVisitorByDni(form.dni.trim());
            const data = response.data;

            setForm((prev) => ({
                ...prev,
                fullName: data.fullName || "",
                company: data.company || "",
                sector: data.sector || APP_CONFIG.DEFAULT_SECTOR,
                photoUrl: data.photoUrl || "",
                photo: null,
            }));

            showFeedback(`Visitante encontrado: ${data.fullName}`, "success");
        } catch (err) {
            showFeedback(err.message || "Visitante no registrado previamente", "info");
        } finally {
            setLoading(false);
        }
    }

    async function handleRegisterVisitor() {
        if (!form.dni.trim() || !form.fullName.trim() || !form.company.trim()) {
            showFeedback("DNI, Nombre Completo y Empresa son obligatorios", "error");
            return;
        }

        if (!form.photo && !form.photoUrl) {
            showFeedback("Es necesario capturar una foto del visitante", "error");
            return;
        }

        try {
            setLoading(true);
            await createVisitor(form);
            showFeedback("Visitante registrado exitosamente", "success");
            await loadDashboard();
        } catch (err) {
            showFeedback(err.message || "No se pudo registrar el visitante", "error");
        } finally {
            setLoading(false);
        }
    }

    async function handleGenerateCredential() {
        if (!form.dni.trim()) {
            showFeedback("Ingrese el DNI del visitante para generar su pase", "error");
            return;
        }

        try {
            setLoading(true);
            const response = await registerEntry(form.dni.trim(), form.sector);
            setVisit(response.data);
            setCredentialOpen(true);
            showFeedback("Pase de ingreso generado", "success");
            await loadDashboard();
        } catch (err) {
            showFeedback(err.message || "Error al generar credencial. Verifique que el visitante esté registrado.", "error");
        } finally {
            setLoading(false);
        }
    }

    async function downloadExcel() {
        try {
            showFeedback("Descargando archivo Excel...", "info");
            const response = await downloadVisitHistoryExcel();
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement("a");
            link.href = url;
            link.setAttribute("download", `visitas-${new Date().toISOString().slice(0, 10)}.xlsx`);
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);
            showFeedback("Excel descargado correctamente", "success");
        } catch (err) {
            showFeedback("No se pudo exportar el historial", "error");
        }
    }

    function cleanForm() {
        setForm({
            dni: "",
            fullName: "",
            company: "",
            sector: APP_CONFIG.DEFAULT_SECTOR,
            photo: null,
            photoUrl: "",
        });
        setVisit(null);
        showFeedback("Formulario reiniciado", "info");
    }

    function closeScanner() {
        setScannerOpen(false);
        loadDashboard();
    }

    const activeInsideCount = useMemo(() => {
        return history.filter((item) => !item.exitTime).length;
    }, [history]);

    const filteredHistory = useMemo(() => {
        if (!tableSearch.trim()) return history;
        const q = tableSearch.toLowerCase();
        return history.filter((item) => {
            const dni = item.visitor?.dni?.toLowerCase() || "";
            const name = item.visitor?.fullName?.toLowerCase() || "";
            const comp = item.visitor?.company?.toLowerCase() || "";
            const sec = item.sector?.toLowerCase() || "";
            return dni.includes(q) || name.includes(q) || comp.includes(q) || sec.includes(q);
        });
    }, [history, tableSearch]);

    return (
        <AppShell wide>
            <Navbar />

            {/* Metrics Header */}
            <section className="metrics-grid">
                <div className="metric-card">
                    <div className="metric-icon-wrap cyan">
                        <IconCalendar size={20} />
                    </div>
                    <div className="metric-info">
                        <strong className="metric-number">{todayCount}</strong>
                        <span className="metric-label">Ingresos de Hoy</span>
                    </div>
                </div>

                <div className="metric-card">
                    <div className="metric-icon-wrap emerald">
                        <IconActivity size={20} />
                    </div>
                    <div className="metric-info">
                        <strong className="metric-number">{activeInsideCount}</strong>
                        <span className="metric-label">Visitantes Activos</span>
                    </div>
                </div>

                <div className="metric-card">
                    <div className="metric-icon-wrap purple">
                        <IconUsers size={20} />
                    </div>
                    <div className="metric-info">
                        <strong className="metric-number">{totalVisitors}</strong>
                        <span className="metric-label">Total en Base de Datos</span>
                    </div>
                </div>

                <div className="metric-card action-metric-card" onClick={downloadExcel} role="button" tabIndex={0}>
                    <div className="metric-icon-wrap orange">
                        <IconDownload size={20} />
                    </div>
                    <div className="metric-info">
                        <strong className="metric-number">Exportar</strong>
                        <span className="metric-label">Descargar Reporte .XLSX</span>
                    </div>
                </div>
            </section>

            {feedback.message && (
                <div className={`feedback-banner ${feedback.type}`}>
                    {feedback.message}
                </div>
            )}

            {/* Main Command Center Grid */}
            <div className="command-center-layout">
                {/* Left Column: Registration & Actions */}
                <section className="panel-card registration-panel">
                    <div className="panel-header">
                        <div className="panel-title-group">
                            <IconFileText size={18} className="panel-title-icon-svg" />
                            <h2 className="panel-title">Registro y Emisión de Pases</h2>
                        </div>
                        <span className="panel-badge">Control de Acceso</span>
                    </div>

                    <div className="form-group">
                        <label className="field-label">Documento de Identidad (DNI)</label>
                        <div className="search-input-row">
                            <input
                                placeholder="Ej: 40123456"
                                value={form.dni}
                                onChange={(e) => updateField("dni", e.target.value)}
                                onKeyDown={(e) => e.key === "Enter" && searchVisitor()}
                            />
                            <button
                                type="button"
                                className="btn-inline-search"
                                onClick={searchVisitor}
                                disabled={loading}
                                title="Buscar visitante registrado"
                            >
                                <IconSearch size={14} className="inline-icon" />
                                <span>Buscar</span>
                            </button>
                        </div>
                    </div>

                    <div className="form-group">
                        <label className="field-label">Nombre y Apellido</label>
                        <input
                            placeholder="Nombre del visitante"
                            value={form.fullName}
                            onChange={(e) => updateField("fullName", e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label className="field-label">Empresa / Organización</label>
                        <input
                            placeholder="Empresa o procedencia"
                            value={form.company}
                            onChange={(e) => updateField("company", e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label className="field-label">Sector a Visitar</label>
                        <select
                            value={form.sector}
                            onChange={(e) => updateField("sector", e.target.value)}
                        >
                            {SECTORS.map((s) => (
                                <option key={s.id} value={s.id}>
                                    {s.name}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label className="field-label">Fotografía del Visitante</label>
                        <CameraCapture onCapture={(photo) => updateField("photo", photo)} />

                        {form.photoUrl && !form.photo && (
                            <div className="existing-photo-badge">
                                <span>Foto en archivo:</span>
                                <img src={form.photoUrl} alt="Foto existente" className="thumbnail-photo" />
                            </div>
                        )}
                    </div>

                    <div className="panel-actions-row">
                        <button
                            type="button"
                            className="btn-action primary"
                            onClick={handleRegisterVisitor}
                            disabled={loading}
                        >
                            <IconCheck size={16} className="inline-icon" />
                            <span>Registrar Datos</span>
                        </button>
                        <button
                            type="button"
                            className="btn-action secondary"
                            onClick={cleanForm}
                            disabled={loading}
                        >
                            <IconRefresh size={14} className="inline-icon" />
                            <span>Limpiar</span>
                        </button>
                    </div>

                    <div className="panel-divider" />

                    <div className="credential-actions-box">
                        <button
                            type="button"
                            className="btn-action btn-issue-pass"
                            onClick={handleGenerateCredential}
                            disabled={loading}
                        >
                            <IconIdCard size={18} className="inline-icon" />
                            <span>Generar Credencial de Acceso</span>
                        </button>

                        <button
                            type="button"
                            className="btn-action btn-scan-exit"
                            onClick={() => setScannerOpen(true)}
                        >
                            <IconQrCode size={18} className="inline-icon" />
                            <span>Escanear QR de Salida</span>
                        </button>
                    </div>
                </section>

                {/* Right Column: Live Activity Monitor */}
                <section className="panel-card monitor-panel">
                    <div className="panel-header">
                        <div>
                            <div className="panel-title-group">
                                <IconTable size={18} className="panel-title-icon-svg" />
                                <h2 className="panel-title">Registro de Visitas en Vivo</h2>
                            </div>
                            <p className="panel-subtitle">Historial de entradas y salidas registradas en el edificio</p>
                        </div>

                        <div className="table-search-box">
                            <input
                                placeholder="Filtrar por DNI, Nombre o Empresa..."
                                value={tableSearch}
                                onChange={(e) => setTableSearch(e.target.value)}
                                className="table-search-input"
                            />
                        </div>
                    </div>

                    <div className="table-responsive-container">
                        {filteredHistory.length === 0 ? (
                            <div className="empty-state-box">
                                <IconFileText size={32} className="empty-state-icon-svg" />
                                <p className="empty-state-text">No se encontraron registros de visitas</p>
                            </div>
                        ) : (
                            <table className="data-table">
                                <thead>
                                    <tr>
                                        <th>Fecha</th>
                                        <th>Ingreso</th>
                                        <th>Salida</th>
                                        <th>DNI</th>
                                        <th>Visitante</th>
                                        <th>Empresa</th>
                                        <th>Sector</th>
                                        <th>Estado</th>
                                        <th>Acción</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {filteredHistory.slice(0, 15).map((item) => {
                                        const isInside = !item.exitTime;
                                        const secConfig = SECTORS.find((s) => s.id === item.sector) || { badgeClass: "badge-cyan" };

                                        return (
                                            <tr key={item.id || item.qrToken} className={isInside ? "row-active-visit" : ""}>
                                                <td className="cell-date">{formatDate(item.entryTime)}</td>
                                                <td className="cell-time">{formatTime(item.entryTime)}</td>
                                                <td className="cell-time">{formatTime(item.exitTime)}</td>
                                                <td className="cell-dni">{item.visitor?.dni || "-"}</td>
                                                <td className="cell-name">{item.visitor?.fullName || "-"}</td>
                                                <td className="cell-company">{item.visitor?.company || "-"}</td>
                                                <td>
                                                    <span className={`table-sector-badge ${secConfig.badgeClass}`}>
                                                        {item.sector || "-"}
                                                    </span>
                                                </td>
                                                <td>
                                                    <span className={`status-pill ${isInside ? "active" : "finished"}`}>
                                                        {isInside && <span className="status-dot-pulse" />}
                                                        {isInside ? "Activo" : "Finalizado"}
                                                    </span>
                                                </td>
                                                <td>
                                                    {isInside && (
                                                        <button
                                                            type="button"
                                                            className="btn-table-view-pass"
                                                            onClick={() => {
                                                                setVisit(item);
                                                                setCredentialOpen(true);
                                                            }}
                                                            title="Ver credencial"
                                                        >
                                                            <IconIdCard size={14} className="inline-icon" />
                                                            <span>Ver Pase</span>
                                                        </button>
                                                    )}
                                                </td>
                                            </tr>
                                        );
                                    })}
                                </tbody>
                            </table>
                        )}
                    </div>
                </section>
            </div>

            {/* Modals */}
            {credentialOpen && visit && (
                <CredentialModal visit={visit} onClose={() => setCredentialOpen(false)} />
            )}

            {scannerOpen && <ScannerModal onClose={closeScanner} />}
        </AppShell>
    );
}
