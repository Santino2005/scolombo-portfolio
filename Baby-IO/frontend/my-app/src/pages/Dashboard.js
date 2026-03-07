import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Dashboard.css';

const Dashboard = ({ user }) => {
    return (
        <div className="dashboard-container">
            <h2 className="dashboard-title">Welcome, {user?.username || 'User'}!</h2>
            <div className="dashboard-cards">
                <div className="card">
                    <h3>Your Babies</h3>
                    <p>Manage your baby profiles</p>
                    <Link to="/babies" className="card-link">Go to Babies</Link>
                </div>
                <div className="card">
                    <h3>Sleep Routines</h3>
                    <p>Create and manage sleep routines</p>
                    <Link to="/routines" className="card-link">Go to Routines</Link>
                </div>
                <div className="card">
                    <h3>Sleep Session</h3>
                    <p>Start a new sleep session</p>
                    <Link to="/session" className="card-link">Go to Session</Link>
                </div>
                <div className="card">
                    <h3>Device Management</h3>
                    <p>Manage system devices</p>
                    <Link to="/devices" className="card-link">Go to Device Management</Link>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;