import { useEffect, useRef } from "react";
import { Html5QrcodeScanner } from "html5-qrcode";
import { registerExit } from "../api/visitApi";

export default function ScannerModal({ onClose }) {
    const scannedRef = useRef(false);

    useEffect(() => {
        const scanner = new Html5QrcodeScanner(
            "qr-reader-modal",
            {
                fps: 10,
                qrbox: {
                    width: 260,
                    height: 260,
                },
                rememberLastUsedCamera: true,
                showTorchButtonIfSupported: true,
            },
            false
        );

        scanner.render(async (decodedText) => {
            if (scannedRef.current) return;

            scannedRef.current = true;

            try {
                await registerExit(decodedText);
                alert("Salida registrada");
                await scanner.clear();
                onClose();
            } catch {
                alert("QR inválido o visita ya cerrada");
                scannedRef.current = false;
            }
        });

        return () => {
            scanner.clear().catch(() => {});
        };
    }, [onClose]);

    return (
        <div className="modal-backdrop">
            <div className="modal-content scanner-modal">
                <section className="card">
                    <h2 className="card-title">Escanear salida</h2>

                    <div id="qr-reader-modal" className="qr-reader-box" />

                    <button className="secondary" onClick={onClose}>
                        ✖ Cerrar
                    </button>
                </section>
            </div>
        </div>
    );
}