import { Navigate } from "react-router-dom";

export default function PrivateRoute({ children }) {
    const logged = localStorage.getItem("guardLogged") === "true";
    return logged ? children : <Navigate to="/login" />;
}