import { useEffect, useRef, useState } from "react";
import { Html5QrcodeScanner } from "html5-qrcode";
import { registerExit } from "../api/visitApi";
import { IconClose } from "./Icons";

export default function ScannerModal({ onClose }) {
    const scannedRef = useRef(false);
    const [statusMessage, setStatusMessage] = useState({ text: "Apunta la cámara al código QR de la credencial", type: "info" });
    const [processing, setProcessing] = useState(false);

    useEffect(() => {
        const scanner = new Html5QrcodeScanner(
            "qr-reader-modal",
            {
                fps: 15,
                qrbox: {
                    width: 240,
                    height: 240,
                },
                rememberLastUsedCamera: true,
                showTorchButtonIfSupported: true,
            },
            false
        );

        scanner.render(
            async (decodedText) => {
                if (scannedRef.current || processing) return;

                scannedRef.current = true;
                setProcessing(true);
                setStatusMessage({ text: "Procesando salida...", type: "info" });

                try {
                    await registerExit(decodedText);
                    setStatusMessage({ text: "Salida registrada exitosamente", type: "success" });
                    setTimeout(async () => {
                        await scanner.clear();
                        onClose();
                    }, 1200);
                } catch (err) {
                    setStatusMessage({
                        text: err.message || "QR inválido o visita ya cerrada",
                        type: "error",
                    });
                    setTimeout(() => {
                        scannedRef.current = false;
                        setProcessing(false);
                        setStatusMessage({ text: "Apunta la cámara al código QR de la credencial", type: "info" });
                    }, 2500);
                }
            },
            () => {
                // Ignore scanning cycle frame errors
            }
        );

        return () => {
            scanner.clear().catch(() => {});
        };
    }, [onClose]);

    return (
        <div className="modal-backdrop" onClick={onClose} role="dialog" aria-modal="true">
            <div className="modal-container scanner-modal-container" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header-bar">
                    <h2 className="modal-title">Escanear QR de Salida</h2>
                    <button type="button" className="btn-modal-close" onClick={onClose} aria-label="Cerrar">
                        <IconClose size={18} />
                    </button>
                </div>

                <div className="modal-body">
                    <div className={`scanner-status-banner ${statusMessage.type}`}>
                        {statusMessage.text}
                    </div>

                    <div className="scanner-viewport-box">
                        <div id="qr-reader-modal" className="qr-reader-canvas" />
                    </div>
                </div>

                <div className="modal-footer-actions">
                    <button type="button" className="btn-action secondary" onClick={onClose}>
                        Cancelar
                    </button>
                </div>
            </div>
        </div>
    );
}
