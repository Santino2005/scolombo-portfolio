import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginGuard, registerGuard } from "../api/guardApi";
import { APP_CONFIG } from "../constants/config";
import { ROUTES } from "../constants/routes";
import { IconUser, IconLock, IconZap } from "../components/Icons";

export default function LoginPage() {
    const [mode, setMode] = useState("login"); // "login" | "register"
    const [username, setUsername] = useState("");
    const [pin, setPin] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [successMessage, setSuccessMessage] = useState("");
    const navigate = useNavigate();

    async function handleSubmit(event) {
        event.preventDefault();
        setError("");
        setSuccessMessage("");

        const cleanUser = username.trim();
        const cleanPin = pin.trim();

        if (!cleanUser || !cleanPin) {
            setError("Por favor complete usuario y PIN");
            return;
        }

        try {
            setLoading(true);
            if (mode === "login") {
                await loginGuard(cleanUser, cleanPin);
                localStorage.setItem(APP_CONFIG.STORAGE_KEYS.GUARD_LOGGED, "true");
                localStorage.setItem(APP_CONFIG.STORAGE_KEYS.GUARD_USER, cleanUser);
                navigate(ROUTES.GUARD);
            } else {
                await registerGuard(cleanUser, cleanPin);
                setSuccessMessage("Guardia registrado con éxito. Ya puede iniciar sesión.");
                setMode("login");
            }
        } catch (err) {
            if (mode === "login") {
                setError(err.message || "Credenciales inválidas. Verifique usuario y PIN.");
            } else {
                setError(err.message || "No se pudo registrar el guardia. Es posible que el usuario ya exista.");
            }
        } finally {
            setLoading(false);
        }
    }

    function handleUseDemo() {
        setUsername("admin");
        setPin("admin");
        setError("");
        setSuccessMessage("");
        setMode("login");
    }

    return (
        <main className="auth-page">
            <div className="auth-card">
                <div className="brand-badge auth-brand-badge">
                    <span className="brand-logo-text">geno</span>
                    <span className="brand-dot"></span>
                </div>

                <h1 className="auth-title">{APP_CONFIG.APP_NAME}</h1>
                <p className="auth-subtitle">{APP_CONFIG.APP_SUBTITLE}</p>

                <div className="auth-tabs">
                    <button
                        type="button"
                        className={`auth-tab ${mode === "login" ? "active" : ""}`}
                        onClick={() => {
                            setMode("login");
                            setError("");
                            setSuccessMessage("");
                        }}
                    >
                        Iniciar Sesión
                    </button>
                    <button
                        type="button"
                        className={`auth-tab ${mode === "register" ? "active" : ""}`}
                        onClick={() => {
                            setMode("register");
                            setError("");
                            setSuccessMessage("");
                        }}
                    >
                        Registrar Guardia
                    </button>
                </div>

                {error && <div className="alert-box error">{error}</div>}
                {successMessage && <div className="alert-box success">{successMessage}</div>}

                <form onSubmit={handleSubmit} className="auth-form">
                    <div className="input-group">
                        <label className="input-label">Usuario</label>
                        <div className="input-wrapper">
                            <span className="input-icon">
                                <IconUser size={16} />
                            </span>
                            <input
                                placeholder="Usuario de guardia"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                disabled={loading}
                                autoFocus
                            />
                        </div>
                    </div>

                    <div className="input-group">
                        <label className="input-label">PIN de Seguridad</label>
                        <div className="input-wrapper">
                            <span className="input-icon">
                                <IconLock size={16} />
                            </span>
                            <input
                                placeholder="••••"
                                type="password"
                                value={pin}
                                onChange={(e) => setPin(e.target.value)}
                                disabled={loading}
                            />
                        </div>
                    </div>

                    <button type="submit" className="btn-primary auth-submit-btn" disabled={loading}>
                        {loading ? (
                            <span className="spinner"></span>
                        ) : mode === "login" ? (
                            "Ingresar al Sistema"
                        ) : (
                            "Crear Cuenta de Guardia"
                        )}
                    </button>
                </form>

                <div className="demo-credentials-box">
                    <div className="demo-title">Acceso rápido para evaluación / demo:</div>
                    <button
                        type="button"
                        className="btn-demo"
                        onClick={handleUseDemo}
                        disabled={loading}
                    >
                        <IconZap size={14} className="inline-icon" />
                        <span>Usar credenciales Demo (admin / admin)</span>
                    </button>
                </div>
            </div>
        </main>
    );
}
