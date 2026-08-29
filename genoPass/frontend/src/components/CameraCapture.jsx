import { useEffect, useRef, useState } from "react";

export default function CameraCapture({ onCapture }) {
    const videoRef = useRef(null);
    const streamRef = useRef(null);
    const [started, setStarted] = useState(false);
    const [preview, setPreview] = useState(null);
    const [error, setError] = useState("");

    async function startCamera() {
        setError("");
        try {
            const stream = await navigator.mediaDevices.getUserMedia({
                video: { width: { ideal: 640 }, height: { ideal: 480 }, facingMode: "user" },
                audio: false,
            });

            streamRef.current = stream;
            if (videoRef.current) {
                videoRef.current.srcObject = stream;
            }
            setStarted(true);
        } catch (err) {
            console.error("Camera access error:", err);
            setError("No se pudo acceder a la cámara. Verifique los permisos del navegador.");
        }
    }

    function stopCamera() {
        if (streamRef.current) {
            streamRef.current.getTracks().forEach((track) => track.stop());
            streamRef.current = null;
        }
        setStarted(false);
    }

    function capturePhoto() {
        if (!videoRef.current) return;
        const video = videoRef.current;
        const canvas = document.createElement("canvas");
        canvas.width = video.videoWidth || 640;
        canvas.height = video.videoHeight || 480;

        const ctx = canvas.getContext("2d");
        ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

        canvas.toBlob(
            (blob) => {
                if (!blob) return;
                const file = new File([blob], `visitor-${Date.now()}.jpg`, { type: "image/jpeg" });
                const previewUrl = URL.createObjectURL(file);
                setPreview(previewUrl);
                onCapture(file);
                stopCamera();
            },
            "image/jpeg",
            0.88
        );
    }

    async function repeatPhoto() {
        if (preview) {
            URL.revokeObjectURL(preview);
        }
        setPreview(null);
        onCapture(null);
        await startCamera();
    }

    useEffect(() => {
        return () => {
            stopCamera();
            if (preview) {
                URL.revokeObjectURL(preview);
            }
        };
    }, []);

    return (
        <div className="camera-widget">
            <div className="camera-viewport-container">
                {preview ? (
                    <div className="camera-preview-wrapper">
                        <img src={preview} alt="Foto capturada del visitante" className="camera-preview-img" />
                        <span className="camera-badge-success">✓ Foto capturada</span>
                    </div>
                ) : (
                    <div className="camera-live-wrapper">
                        <video ref={videoRef} autoPlay playsInline muted className={`camera-video ${started ? "active" : "inactive"}`} />
                        {!started && !error && (
                            <div className="camera-placeholder">
                                <span className="camera-placeholder-icon">📸</span>
                                <span className="camera-placeholder-text">Cámara lista para captura</span>
                            </div>
                        )}
                        {started && <div className="camera-crosshair"></div>}
                    </div>
                )}
            </div>

            {error && <p className="camera-error-msg">{error}</p>}

            <div className="camera-controls">
                {!started && !preview && (
                    <button type="button" className="btn-camera primary" onClick={startCamera}>
                        📷 Iniciar Cámara
                    </button>
                )}

                {started && !preview && (
                    <button type="button" className="btn-camera btn-capture" onClick={capturePhoto}>
                        ⚡ Capturar Fotografía
                    </button>
                )}

                {preview && (
                    <button type="button" className="btn-camera secondary" onClick={repeatPhoto}>
                        🔄 Tomar Otra Foto
                    </button>
                )}
            </div>
        </div>
    );
}
