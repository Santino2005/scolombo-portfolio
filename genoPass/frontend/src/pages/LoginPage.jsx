import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginGuard } from "../api/guardApi";
import { APP_CONFIG } from "../constants/config";
import { ROUTES } from "../constants/routes";

export default function LoginPage() {
    const [username, setUsername] = useState("");
    const [pin, setPin] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    async function handleLogin(event) {
        event.preventDefault();
        setError("");

        if (!username.trim() || !pin.trim()) {
            setError("Por favor complete usuario y PIN");
            return;
        }

        try {
            setLoading(true);
            await loginGuard(username.trim(), pin.trim());
            localStorage.setItem(APP_CONFIG.STORAGE_KEYS.GUARD_LOGGED, "true");
            localStorage.setItem(APP_CONFIG.STORAGE_KEYS.GUARD_USER, username.trim());
            navigate(ROUTES.GUARD);
        } catch (err) {
            setError(err.message || "Credenciales inválidas. Verifique usuario y PIN.");
        } finally {
            setLoading(false);
        }
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

                {error && <div className="alert-box error">{error}</div>}

                <form onSubmit={handleLogin} className="auth-form">
                    <div className="input-group">
                        <label className="input-label">Usuario</label>
                        <div className="input-wrapper">
                            <span className="input-icon">👤</span>
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
                            <span className="input-icon">🔒</span>
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
                        {loading ? <span className="spinner"></span> : "Ingresar al Sistema"}
                    </button>
                </form>
            </div>
        </main>
    );
}
