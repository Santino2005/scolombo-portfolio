import React, { useState, useEffect } from 'react';
import { apiCall } from '../config/Api';
import SensorControlPanel from '../components/SensorControlPanel.js';
import LullabyPlayerControlPanel from '../components/LullabyPlayerControlPanel.js';
import '../styles/DeviceManagement.css';

const DeviceManagement = () => {
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [sensorTypes, setSensorTypes] = useState([]);

    useEffect(() => {
        const fetchDeviceData = async () => {
            try {
                setLoading(true);
                setError('');
                // Fetch sensor types
                const types = await apiCall('/api/auth/me/sensors/list');
                setSensorTypes(types);
            } catch (err) {
                console.error('Device data fetch error:', err);
                setError('Failed to load device data. Please try again later.');
            } finally {
                setLoading(false);
            }
        };

        fetchDeviceData();
        const intervalId = setInterval(fetchDeviceData, 30000);
        return () => clearInterval(intervalId);
    }, []);

    if (loading) {
        return (
            <div className="loading-container">
                <div className="spinner"></div>
                <p>Loading device information...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="error-container">
                <p className="error-message">{error}</p>
                <button
                    className="retry-button"
                    onClick={() => window.location.reload()}
                >
                    Retry
                </button>
            </div>
        );
    }

    return (
        <div className="device-management-container">
            <header className="device-header">
                <h1>Device Management</h1>
                <p className="subtitle">Monitor and control your connected devices</p>
            </header>

            <div className="device-control-panels">
                <div className="control-panel-section">
                    <h2>Sensor Controls</h2>
                    <SensorControlPanel sensorTypes={sensorTypes} />
                </div>

                <div className="control-panel-section">
                    <h2>Lullaby Player</h2>
                    <LullabyPlayerControlPanel />
                </div>
            </div>
        </div>
    );
};

export default DeviceManagement;