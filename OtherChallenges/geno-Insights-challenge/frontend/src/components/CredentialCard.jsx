import { QRCodeCanvas } from "qrcode.react";

export default function CredentialCard({ visit }) {
    const visitor = visit.visitor;

    return (
        <section className="public-credential-card">
            <div className="credential-brand">GENO INSIGHTS</div>
            <div className="credential-subtitle">CONTROL DE INGRESOS PRO</div>

            <div className="credential-photo-wrap">
                {visitor?.photoUrl ? (
                    <img src={visitor.photoUrl} alt="Visitante" className="credential-photo" />
                ) : (
                    <div className="credential-photo placeholder" />
                )}
            </div>

            <h3>{visitor?.fullName}</h3>

            <span className="sector-pill">{visit.sector}</span>

            <div className="credential-separator" />

            <QRCodeCanvas value={visit.qrToken} size={180} />

            <p className="dni-text">DNI: {visitor?.dni}</p>

            <div className="credential-separator" />

            <p className="credential-footer">
                Credencial activa hasta registrar salida
            </p>
        </section>
    );
}