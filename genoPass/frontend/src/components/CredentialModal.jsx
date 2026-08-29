import CredentialCard from "./CredentialCard";

export default function CredentialModal({ visit, onClose }) {
    if (!visit) return null;

    function handlePrint() {
        window.print();
    }

    return (
        <div className="modal-backdrop" onClick={onClose} role="dialog" aria-modal="true">
            <div className="modal-container credential-modal-container" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header-bar">
                    <h2 className="modal-title">Pase de Acceso Generado</h2>
                    <button type="button" className="btn-modal-close" onClick={onClose} aria-label="Cerrar">
                        ✕
                    </button>
                </div>

                <div className="modal-body print-area">
                    <CredentialCard visit={visit} />
                </div>

                <div className="modal-footer-actions no-print">
                    <button type="button" className="btn-action primary" onClick={handlePrint}>
                        🖨️ Imprimir / Guardar PDF
                    </button>
                    <button type="button" className="btn-action secondary" onClick={onClose}>
                        Cerrar
                    </button>
                </div>
            </div>
        </div>
    );
}
