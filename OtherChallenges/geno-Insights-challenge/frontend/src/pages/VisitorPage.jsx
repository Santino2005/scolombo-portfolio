import { useState } from "react";
import { getActiveCredentialByDni, registerEntry } from "../api/visitApi";
import AppShell from "../components/AppShell";
import Navbar from "../components/Navbar";
import CredentialModal from "../components/CredentialModal";

export default function VisitorPage() {
    const [dni, setDni] = useState("");
    const [sector, setSector] = useState("Operaciones");
    const [visit, setVisit] = useState(null);
    const [credentialOpen, setCredentialOpen] = useState(false);
    const [loading, setLoading] = useState(false);

    const sectors = [
        "Administración",
        "Operaciones",
        "Logística",
        "Almacén",
        "Seguridad",
        "Recepción",
        "Mantenimiento",
    ];

    async function viewCredential() {
        if (!dni.trim()) {
            alert("Ingresá tu DNI");
            return;
        }

        try {
            setLoading(true);
            const response = await getActiveCredentialByDni(dni);

            setVisit(response.data);
            setCredentialOpen(true);
        } catch {
            alert("No tenés una credencial activa");
        } finally {
            setLoading(false);
        }
    }

    async function generateEntryQr() {
        if (!dni.trim()) {
            alert("Ingresá tu DNI");
            return;
        }

        try {
            setLoading(true);
            const response = await registerEntry(dni, sector);

            setVisit(response.data);
            setCredentialOpen(true);
        } catch {
            alert("No se pudo generar QR de ingreso. Verificá que estés registrado.");
        } finally {
            setLoading(false);
        }
    }

    return (
        <AppShell>
            <Navbar />

            <section className="card">
                <h2 className="card-title">Portal del Visitante</h2>

                <label className="field-label">DNI</label>
                <input
                    placeholder="Ej: 46742185"
                    value={dni}
                    onChange={(e) => setDni(e.target.value)}
                />

                <label className="field-label">Sector</label>
                <select
                    value={sector}
                    onChange={(e) => setSector(e.target.value)}
                >
                    {sectors.map((sector) => (
                        <option key={sector} value={sector}>
                            {sector}
                        </option>
                    ))}
                </select>

                <button
                    className="primary wide-button"
                    onClick={generateEntryQr}
                    disabled={loading}
                >
                    {loading ? "Generando..." : "🎟️ Generar QR de ingreso"}
                </button>

                <button
                    className="wide-button"
                    onClick={viewCredential}
                    disabled={loading}
                >
                    {loading ? "Buscando..." : "🪪 Ver mi credencial activa"}
                </button>
            </section>

            {credentialOpen && visit && (
                <CredentialModal
                    visit={visit}
                    onClose={() => setCredentialOpen(false)}
                />
            )}
        </AppShell>
    );
}