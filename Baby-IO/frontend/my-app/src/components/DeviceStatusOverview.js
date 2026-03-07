// src/components/device/DeviceStatusOverview.js
import React, { useState, useEffect } from 'react';
import { apiCall } from '../config/Api';
import {
    FaThermometerHalf,
    FaTint,
    FaRunning,
    FaVolumeUp,
    FaMusic
} from 'react-icons/fa';
import {
    IoPower,
    IoRefresh,
    IoPlay,
    IoPause,
    IoStop
} from 'react-icons/io5';

const DeviceStatusOverview = () => {
    const [status, setStatus] = useState({
        temperature: null,
        humidity: null,
        motion: null,
        sound: null,
        lullabyPlayer: null
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStatus = async () => {
            try {
                const [sensorStatus, playerStatus] = await Promise.all([
                    apiCall('/api/auth/me/sensors/status/all'),
                    apiCall('/api/auth/me/lullaby-player/status')
                ]);

                // Organize sensors by type for easier access
                const sensors = {
                    temperature: sensorStatus.find(s => s.type === 'TEMPERATURE'),
                    humidity: sensorStatus.find(s => s.type === 'HUMIDITY'),
                    motion: sensorStatus.find(s => s.type === 'MOTION'),
                    sound: sensorStatus.find(s => s.type === 'SOUND')
                };

                setStatus({
                    ...sensors,
                    lullabyPlayer: playerStatus
                });
                setLoading(false);
            } catch (err) {
                console.error('Error fetching device status:', err);
                setLoading(false);
            }
        };

        fetchStatus();
        const interval = setInterval(fetchStatus, 5000);

        return () => clearInterval(interval);
    }, []);

    const handleSensorAction = async (sensorType, action) => {
        try {
            await apiCall(`/api/auth/me/sensors/${action}/${sensorType}`, 'POST');
            // Refetch status after action
            const sensorStatus = await apiCall('/api/auth/me/sensors/status/all');
            const updatedSensor = sensorStatus.find(s => s.type === sensorType);
            setStatus(prev => ({ ...prev, [sensorType.toLowerCase()]: updatedSensor }));
        } catch (err) {
            console.error(`Error ${action} sensor ${sensorType}:`, err);
        }
    };

    const handlePlayerAction = async (action) => {
        try {
            await apiCall(`/api/auth/me/lullaby-player/${action}`, 'POST');
            const playerStatus = await apiCall('/api/auth/me/lullaby-player/status');
            setStatus(prev => ({ ...prev, lullabyPlayer: playerStatus }));
        } catch (err) {
            console.error(`Error ${action} player:`, err);
        }
    };

    if (loading) return <div className="loading">Loading device status...</div>;

    const SensorCard = ({ type, sensor }) => {
        const icons = {
            TEMPERATURE: <FaThermometerHalf size={24} />,
            HUMIDITY: <FaTint size={24} />,
            MOTION: <FaRunning size={24} />,
            SOUND: <FaVolumeUp size={24} />
        };

        return (
            <div className={`status-card ${type.toLowerCase()}`}>
                <div className="card-header">
                    <div className="sensor-icon">{icons[type]}</div>
                    <h3>{type.charAt(0) + type.slice(1).toLowerCase()}</h3>
                </div>
                <div className="card-body">
                    <div className={`status-indicator ${sensor?.connected ? 'connected' : 'disconnected'}`}>
                        {sensor?.connected ? 'Connected' : 'Disconnected'}
                    </div>
                    <div className={`status-indicator ${sensor?.enabled ? 'enabled' : 'disabled'}`}>
                        {sensor?.enabled ? 'Enabled' : 'Disabled'}
                    </div>
                    {sensor?.value && (
                        <div className="sensor-value">
                            Value: {sensor.value}
                        </div>
                    )}
                </div>
                <div className="card-actions">
                    <button
                        onClick={() => handleSensorAction(type, sensor?.enabled ? 'disable' : 'enable')}
                        className={`action-btn ${sensor?.enabled ? 'disable' : 'enable'}`}
                        disabled={!sensor?.connected}
                    >
                        <IoPower /> {sensor?.enabled ? 'Disable' : 'Enable'}
                    </button>
                    <button
                        onClick={() => handleSensorAction(type, 'restart')}
                        className="action-btn restart"
                        disabled={!sensor?.connected}
                    >
                        <IoRefresh /> Restart
                    </button>
                </div>
            </div>
        );
    };

    return (
        <div className="device-status-overview">
            <h2>Device Status Overview</h2>
            <div className="status-grid">
                <SensorCard type="TEMPERATURE" sensor={status.temperature} />
                <SensorCard type="HUMIDITY" sensor={status.humidity} />
                <SensorCard type="MOTION" sensor={status.motion} />
                <SensorCard type="SOUND" sensor={status.sound} />

                <div className={`status-card player`}>
                    <div className="card-header">
                        <div className="player-icon"><FaMusic size={24} /></div>
                        <h3>Lullaby Player</h3>
                    </div>
                    <div className="card-body">
                        <div className={`status-indicator ${status.lullabyPlayer?.enabled ? 'enabled' : 'disabled'}`}>
                            {status.lullabyPlayer?.enabled ? 'Enabled' : 'Disabled'}
                        </div>
                        {status.lullabyPlayer?.currentSong && (
                            <div className="now-playing">
                                Now Playing: {status.lullabyPlayer.currentSong}
                            </div>
                        )}
                    </div>
                    <div className="card-actions">
                        <button
                            onClick={() => handlePlayerAction('play')}
                            className="action-btn play"
                            disabled={!status.lullabyPlayer?.enabled}
                        >
                            <IoPlay /> Play
                        </button>
                        <button
                            onClick={() => handlePlayerAction('pause')}
                            className="action-btn pause"
                            disabled={!status.lullabyPlayer?.enabled}
                        >
                            <IoPause /> Pause
                        </button>
                        <button
                            onClick={() => handlePlayerAction('stop')}
                            className="action-btn stop"
                            disabled={!status.lullabyPlayer?.enabled}
                        >
                            <IoStop /> Stop
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DeviceStatusOverview;