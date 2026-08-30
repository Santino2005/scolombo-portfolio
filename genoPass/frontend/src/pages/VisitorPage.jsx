import { useState } from "react";
import { getActiveCredentialByDni, registerEntry } from "../api/visitApi";
import { findVisitorByDni } from "../api/visitorApi";
import { SECTORS } from "../constants/sectors";
import { APP_CONFIG } from "../constants/config";

import AppShell from "../components/AppShell";
import Navbar from "../components/Navbar";
import CredentialModal from "../components/CredentialModal";
import { IconTicket, IconIdCard, IconUser, IconSearch, IconQrCode } from "../components/Icons";

export default function VisitorPage() {
    const [dni, setDni] = useState("");
    const [sector, setSector] = useState(APP_CONFIG.DEFAULT_SECTOR);
    const [visit, setVisit] = useState(null);
    const [credentialOpen, setCredentialOpen] = useState(false);
    const [loading, setLoading] = useState(false);
    const [feedback, setFeedback] = useState({ message: "", type: "" });
    const [visitorPreview, setVisitorPreview] = useState(null);

    function showFeedback(message, type = "info") {
        setFeedback({ message, type });
    }

    async function checkVisitorRegistration() {
        if (!dni.trim()) {
            showFeedback("Por favor ingrese su número de DNI", "error");
            return;
        }

        try {
            setLoading(true);
            const res = await findVisitorByDni(dni.trim());
            setVisitorPreview(res.data);
            showFeedback(`Hola ${res.data.fullName}, seleccione su sector de destino`, "success");
        } catch (err) {
            setVisitorPreview(null);
            showFeedback("DNI no registrado. Por favor acérquese al puesto de guardia para su registro inicial.", "error");
        } finally {
            setLoading(false);
        }
    }

    async function handleGenerateEntryQr() {
        if (!dni.trim()) {
            showFeedback("Por favor ingrese su número de DNI", "error");
            return;
        }

        try {
            setLoading(true);
            const response = await registerEntry(dni.trim(), sector);
            setVisit(response.data);
            setCredentialOpen(true);
            showFeedback("Pase de acceso generado correctamente", "success");
        } catch (err) {
            showFeedback(err.message || "No se pudo generar el QR de ingreso. Verifique si ya tiene una visita activa.", "error");
        } finally {
            setLoading(false);
        }
    }

    async function handleViewActiveCredential() {
        if (!dni.trim()) {
            showFeedback("Por favor ingrese su número de DNI", "error");
            return;
        }

        try {
            setLoading(true);
            const response = await getActiveCredentialByDni(dni.trim());
            setVisit(response.data);
            setCredentialOpen(true);
            showFeedback("Credencial activa encontrada", "success");
        } catch (err) {
            showFeedback("No posee ninguna credencial activa en este momento", "info");
        } finally {
            setLoading(false);
        }
    }

    return (
        <AppShell>
            <Navbar />

            <section className="panel-card visitor-kiosk-card">
                <div className="kiosk-header">
                    <div className="kiosk-icon">
                        <IconTicket size={28} />
                    </div>
                    <h2 className="kiosk-title">Portal de Autogestión</h2>
                    <p className="kiosk-subtitle">Emisión de credencial digital y consulta de pases de acceso</p>
                </div>

                {feedback.message && (
                    <div className={`feedback-banner ${feedback.type}`}>
                        {feedback.message}
                    </div>
                )}

                <div className="form-group">
                    <label className="field-label">Número de Documento (DNI)</label>
                    <div className="search-input-row">
                        <input
                            placeholder="Ej: 40123456"
                            value={dni}
                            onChange={(e) => {
                                setDni(e.target.value);
                                setVisitorPreview(null);
                            }}
                            onBlur={() => dni.trim() && checkVisitorRegistration()}
                        />
                        <button
                            type="button"
                            className="btn-inline-search"
                            onClick={checkVisitorRegistration}
                            disabled={loading}
                        >
                            <IconSearch size={14} className="inline-icon" />
                            <span>Verificar</span>
                        </button>
                    </div>
                </div>

                {visitorPreview && (
                    <div className="visitor-welcome-card">
                        <div className="visitor-avatar">
                            {visitorPreview.photoUrl ? (
                                <img src={visitorPreview.photoUrl} alt="Foto" className="kiosk-avatar-img" />
                            ) : (
                                <IconUser size={24} className="avatar-placeholder-icon" />
                            )}
                        </div>
                        <div className="visitor-welcome-text">
                            <strong>{visitorPreview.fullName}</strong>
                            <span>{visitorPreview.company}</span>
                        </div>
                    </div>
                )}

                <div className="form-group">
                    <label className="field-label">Sector al que se dirige</label>
                    <select
                        value={sector}
                        onChange={(e) => setSector(e.target.value)}
                        className="kiosk-select"
                    >
                        {SECTORS.map((s) => (
                            <option key={s.id} value={s.id}>
                                {s.name}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="kiosk-actions">
                    <button
                        type="button"
                        className="btn-primary kiosk-btn-main"
                        onClick={handleGenerateEntryQr}
                        disabled={loading}
                    >
                        <IconQrCode size={18} className="inline-icon" />
                        <span>{loading ? "Generando..." : "Generar Pase de Ingreso (QR)"}</span>
                    </button>

                    <button
                        type="button"
                        className="btn-secondary kiosk-btn-sub"
                        onClick={handleViewActiveCredential}
                        disabled={loading}
                    >
                        <IconIdCard size={18} className="inline-icon" />
                        <span>{loading ? "Buscando..." : "Ver Mi Credencial Activa"}</span>
                    </button>
                </div>
            </section>

            {credentialOpen && visit && (
                <CredentialModal visit={visit} onClose={() => setCredentialOpen(false)} />
            )}
        </AppShell>
    );
}
