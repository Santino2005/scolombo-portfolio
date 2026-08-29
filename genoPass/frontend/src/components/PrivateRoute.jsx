import { Navigate } from "react-router-dom";
import { APP_CONFIG } from "../constants/config";
import { ROUTES } from "../constants/routes";

export default function PrivateRoute({ children }) {
    const isLogged = localStorage.getItem(APP_CONFIG.STORAGE_KEYS.GUARD_LOGGED) === "true";
    return isLogged ? children : <Navigate to={ROUTES.LOGIN} replace />;
}
