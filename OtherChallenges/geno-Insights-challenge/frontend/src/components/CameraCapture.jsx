import { useEffect, useRef, useState } from "react";

export default function CameraCapture({ onCapture }) {
    const videoRef = useRef(null);
    const streamRef = useRef(null);
    const [started, setStarted] = useState(false);
    const [preview, setPreview] = useState(null);

    async function startCamera() {
        const stream = await navigator.mediaDevices.getUserMedia({
            video: true,
        });

        streamRef.current = stream;

        if (videoRef.current) {
            videoRef.current.srcObject = stream;
        }

        setStarted(true);
    }

    function stopCamera() {
        if (!streamRef.current) return;

        streamRef.current.getTracks().forEach((track) => {
            track.stop();
        });

        streamRef.current = null;
        setStarted(false);
    }

    function capturePhoto() {
        const canvas = document.createElement("canvas");

        canvas.width = videoRef.current.videoWidth;
        canvas.height = videoRef.current.videoHeight;

        canvas.getContext("2d").drawImage(videoRef.current, 0, 0);

        canvas.toBlob((blob) => {
            const file = new File([blob], "visitor-photo.jpg", {
                type: "image/jpeg",
            });

            setPreview(URL.createObjectURL(file));
            onCapture(file);
            stopCamera();
        }, "image/jpeg");
    }

    async function repeatPhoto() {
        setPreview(null);
        onCapture(null);
        await startCamera();
    }

    useEffect(() => {
        return () => {
            stopCamera();
        };
    }, []);

    return (
        <div className="camera-box">
            {preview ? (
                <img
                    src={preview}
                    alt="Foto capturada"
                    className="camera"
                />
            ) : (
                <video
                    ref={videoRef}
                    autoPlay
                    playsInline
                    muted
                    className="camera"
                />
            )}

            {!started && !preview && (
                <button onClick={startCamera}>
                    📷 Activar cámara
                </button>
            )}

            {started && !preview && (
                <button onClick={capturePhoto}>
                    📸 Capturar foto
                </button>
            )}

            {preview && (
                <button onClick={repeatPhoto}>
                    🔁 Repetir foto
                </button>
            )}
        </div>
    );
}