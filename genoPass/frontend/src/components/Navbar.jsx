import { Link, useLocation, useNavigate } from "react-router-dom";
import { APP_CONFIG } from "../constants/config";
import { ROUTES } from "../constants/routes";

export default function Navbar() {
    const navigate = useNavigate();
    const location = useLocation();

    function logout() {
        localStorage.removeItem(APP_CONFIG.STORAGE_KEYS.GUARD_LOGGED);
        localStorage.removeItem(APP_CONFIG.STORAGE_KEYS.GUARD_USER);
        navigate(ROUTES.LOGIN);
    }

    const isGuard = location.pathname === ROUTES.GUARD;
    const isVisitor = location.pathname === ROUTES.VISITOR;

    return (
        <nav className="navbar">
            <div className="navbar-links">
                <Link to={ROUTES.GUARD} className={`nav-link ${isGuard ? "nav-link-active" : ""}`}>
                    <span className="nav-icon">🛡️</span>
                    <span>Panel Guardia</span>
                </Link>
                <Link to={ROUTES.VISITOR} className={`nav-link ${isVisitor ? "nav-link-active" : ""}`}>
                    <span className="nav-icon">👤</span>
                    <span>Portal Visitante</span>
                </Link>
            </div>
            <button type="button" onClick={logout} className="nav-logout-btn" title="Cerrar sesión">
                <span>🚪</span>
                <span className="logout-text">Salir</span>
            </button>
        </nav>
    );
}
