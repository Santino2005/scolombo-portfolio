import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/NavBar.css';

const Navbar = ({ user, currentBaby, onLogout, activeSession }) => {
    const navigate = useNavigate();
    const [localBaby, setLocalBaby] = useState(currentBaby);

    // Sync local baby state with props
    useEffect(() => {
        setLocalBaby(currentBaby);
    }, [currentBaby]);

    const handleSwitchBaby = () => {
        navigate('/babies');
    };

    return (
        <nav className="navbar">
            <div className="nav-links">
                <Link to="/">Dashboard</Link>
                <Link to="/session">Sleep Session</Link>
                <Link to="/devices">Devices</Link>
                {!activeSession && (
                    <>
                        <Link to="/babies">Baby Management</Link>
                        <Link to="/routines">Sleep Routines</Link>
                        <Link to="/history">Sleep History</Link>
                    </>
                )}
            </div>

            <div className="navbar-right">
                <div className="user-info">
                    <span className="info-item">User: {user?.username || 'Unknown'}</span>
                    {localBaby && (
                        <span className="info-item">Baby: {localBaby.name}</span>
                    )}
                </div>

                <div className="navbar-actions">
                    {localBaby && activeSession && (
                        <span className="session-indicator">● Active Session</span>
                    )}
                    {localBaby && (
                        <button
                            className="btn-text btn-switch"
                            onClick={handleSwitchBaby}
                        >
                            Switch Baby
                        </button>
                    )}
                    <button className="btn-text" onClick={onLogout}>
                        Logout
                    </button>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;