import React from 'react';
import { useNavigate, Outlet } from 'react-router-dom';
import logo from './assets/logo.png';
import './App.css';

function Layout() {
    const navigate = useNavigate();

    return (
        <div className="layout">
            <img src={logo} alt="Logo" className="logo clickable-logo" onClick={() => navigate('/')} />
            <Outlet />
        </div>
    );
}

export default Layout;
