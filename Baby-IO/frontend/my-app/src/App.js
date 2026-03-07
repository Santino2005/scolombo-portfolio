// src/App.js
import React, { useState, useEffect, useCallback } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { tokenManager, apiCall } from './config/Api';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import BabyManagement from './pages/BabyManagement';
import SleepRoutines from './pages/SleepRoutines';
import SleepSession from './pages/SleepSession';
import SleepHistory from './pages/SleepHistory';
import Navbar from './components/NavBar';
import DeviceManagement from "./pages/DeviceManagement";
import './App.css';

function App() {
    const [user, setUser] = useState(null);
    const [isAuth, setIsAuth] = useState(false);
    const [isCheckingAuth, setIsCheckingAuth] = useState(true);
    const [currentBaby, setCurrentBaby] = useState(null);
    const [activeSession, setActiveSession] = useState(null);

    const handleLogout = useCallback(async () => {
        try {
            if (activeSession) {
                await apiCall('/api/auth/me/sleep/session/current/stop', 'POST');
            }
        } catch (error) {
            console.error('Error stopping session on logout:', error);
        } finally {
            tokenManager.removeToken();
            localStorage.removeItem('user');
            localStorage.removeItem('currentBaby');
            setUser(null);
            setIsAuth(false);
            setCurrentBaby(null);
            setActiveSession(null);
        }
    }, [activeSession]);

    const checkAuthStatus = useCallback(async () => {
        try {
            if (!tokenManager.isAuthenticated()) {
                setIsAuth(false);
                setIsCheckingAuth(false);
                return;
            }

            const storedUser = localStorage.getItem('user');
            if (storedUser) {
                const userData = JSON.parse(storedUser);
                setUser(userData);
                setIsAuth(true);

                try {
                    const selectedBaby = await apiCall('/api/auth/me/babies/selected');
                    if (selectedBaby) {
                        setCurrentBaby(selectedBaby);
                        localStorage.setItem('currentBaby', JSON.stringify(selectedBaby));
                        // Update user state with selected baby
                        setUser(prev => prev ? { ...prev, selectedBaby } : null);
                    }

                    const session = await apiCall('/api/auth/me/sleep/session/current');
                    if (session) {
                        setActiveSession(session);
                    }
                } catch (error) {
                    console.log('No selected baby or active session found');
                }
            }
            setIsCheckingAuth(false);
        } catch (error) {
            console.error('Error checking auth status:', error);
            handleLogout();
        }
    }, [handleLogout]);

    useEffect(() => {
        checkAuthStatus();
    }, [checkAuthStatus]);

    const handleBabySelect = useCallback((baby) => {
        setCurrentBaby(baby);
        localStorage.setItem('currentBaby', JSON.stringify(baby));
        // Also update the user state if needed
        setUser(prev => prev ? { ...prev, selectedBaby: baby } : null);
    }, []);

    if (isCheckingAuth) {
        return <div className="loading">Loading...</div>;
    }

    return (
        <Router>
            {isAuth && <Navbar
                user={user}
                currentBaby={currentBaby}
                onLogout={handleLogout}
                activeSession={activeSession}
            />}

            <div className="container">
                <Routes>
                    {!isAuth ? (
                        <>
                            <Route path="/login" element={<Login onAuthSuccess={checkAuthStatus} />} />
                            <Route path="/register" element={<Register onAuthSuccess={checkAuthStatus} />} />
                            <Route path="*" element={<Navigate to="/login" replace />} />
                        </>
                    ) : (
                        <>
                            <Route path="/" element={<Dashboard user={user} />} />
                            <Route path="/babies" element={<BabyManagement onBabySelect={handleBabySelect} />} />
                            <Route path="/routines" element={<SleepRoutines />} />
                            <Route path="/devices" element={<DeviceManagement />} />
                            <Route path="/session" element={
                                currentBaby ? (
                                    <SleepSession
                                        currentBaby={currentBaby}
                                        activeSession={activeSession}
                                        setActiveSession={setActiveSession}
                                        setCurrentBaby={setCurrentBaby}
                                    />
                                ) : (
                                    <Navigate to="/babies" replace />
                                )
                            } />
                            <Route path="/history" element={
                                currentBaby ? (
                                    <SleepHistory currentBaby={currentBaby} />
                                ) : (
                                    <Navigate to="/babies" replace />
                                )
                            } />
                            <Route path="*" element={<Navigate to="/" replace />} />
                        </>
                    )}
                </Routes>
            </div>
        </Router>
    );
}

export default App;