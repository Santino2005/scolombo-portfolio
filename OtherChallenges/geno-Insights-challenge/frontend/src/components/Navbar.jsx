import { Link, useNavigate } from "react-router-dom";

export default function Navbar() {
    const navigate = useNavigate();

    function logout() {
        localStorage.removeItem("guardLogged");
        navigate("/login");
    }

    return (
        <nav className="navbar">
            <Link to="/guard">Guard</Link>
            <Link to="/visitor">Visitor</Link>
            <button onClick={logout}>Salir</button>
        </nav>
    );
}