import { QRCodeCanvas } from "qrcode.react";

export default function CredentialModal({ visit, onClose }) {
    const visitor = visit.visitor;

    console.log("VISIT:", visit);
    console.log("VISITOR:", visitor);
    console.log("PHOTO URL:", visitor?.photoUrl);

    return (
        <div className="modal-backdrop">
            <div className="credential-modal-box">
                <section className="credential-card">
                    <div className="credential-brand">GENO INSIGHTS</div>
                    <div className="credential-subtitle">CONTROL DE INGRESOS PRO</div>

                    <div className="credential-photo-wrap">
                        {visitor?.photoUrl ? (
                            <img
                                src={visitor.photoUrl}
                                alt="Visitante"
                                className="credential-photo"
                                referrerPolicy="no-referrer"
                                onLoad={() => console.log("Foto cargada")}
                                onError={(event) => {
                                    console.log("Error cargando foto", event);
                                }}
                            />
                        ) : (
                            <div className="credential-photo placeholder" />
                        )}
                    </div>

                    <h3>{visitor?.fullName}</h3>

                    <span className="sector-pill">
                        {visit.sector}
                    </span>

                    <div className="credential-separator" />

                    <QRCodeCanvas
                        value={visit.qrToken}
                        size={130}
                    />

                    <p className="dni-text">
                        DNI: {visitor?.dni}
                    </p>

                    <div className="credential-separator" />

                    <p className="credential-footer">
                        QR válido hasta registrar salida
                    </p>
                </section>

                <div className="modal-actions">
                    <button className="primary" onClick={() => window.print()}>
                        🖨️ Imprimir
                    </button>

                    <button className="secondary" onClick={onClose}>
                        ✖ Cerrar
                    </button>
                </div>
            </div>
        </div>
    );
}