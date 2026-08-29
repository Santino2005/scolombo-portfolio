import { QRCodeCanvas } from "qrcode.react";
import { SECTORS } from "../constants/sectors";
import { formatDateTime } from "../utils/dateFormatter";

export default function CredentialCard({ visit }) {
    const visitor = visit?.visitor;
    const sectorConfig = SECTORS.find((s) => s.id === visit?.sector) || { badgeClass: "badge-cyan" };

    return (
        <article className="credential-badge-card">
            <div className="badge-lanyard-hole"></div>

            <header className="badge-header">
                <div className="badge-logo-row">
                    <span className="badge-logo-mark">geno</span>
                    <span className="badge-type-tag">CREDENCIAL DE ACCESO</span>
                </div>
                <div className="badge-subtitle">CONTROL DE INGRESOS PRO</div>
            </header>

            <div className="badge-photo-container">
                {visitor?.photoUrl ? (
                    <img
                        src={visitor.photoUrl}
                        alt={`Foto de ${visitor.fullName || "Visitante"}`}
                        className="badge-photo-img"
                        referrerPolicy="no-referrer"
                    />
                ) : (
                    <div className="badge-photo-placeholder">👤</div>
                )}
                <span className="badge-verified-dot" title="Identidad Verificada">✓</span>
            </div>

            <div className="badge-identity">
                <h3 className="badge-name">{visitor?.fullName || "Visitante"}</h3>
                {visitor?.company && <p className="badge-company">🏢 {visitor.company}</p>}
                <div className="badge-sector-wrap">
                    <span className={`badge-sector-pill ${sectorConfig.badgeClass}`}>
                        {visit?.sector || "General"}
                    </span>
                </div>
            </div>

            <div className="badge-qr-box">
                <div className="qr-wrapper-inner">
                    <QRCodeCanvas
                        value={visit?.qrToken || "no-token"}
                        size={150}
                        bgColor="#ffffff"
                        fgColor="#0a192f"
                        level="H"
                    />
                </div>
                <p className="badge-qr-hint">Escanear para registrar salida</p>
            </div>

            <footer className="badge-footer-info">
                <div className="badge-data-row">
                    <span className="badge-data-label">DNI:</span>
                    <span className="badge-data-val">{visitor?.dni || "-"}</span>
                </div>
                {visit?.entryTime && (
                    <div className="badge-data-row">
                        <span className="badge-data-label">Ingreso:</span>
                        <span className="badge-data-val">{formatDateTime(visit.entryTime)}</span>
                    </div>
                )}
            </footer>
        </article>
    );
}
