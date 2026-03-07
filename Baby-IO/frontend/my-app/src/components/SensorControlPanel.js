// src/components/device/SensorControlPanel.js
import React, { useState } from 'react';
import { apiCall } from '../config/Api';

const SensorControlPanel = ({ sensorTypes }) => {
    const [selectedSensor, setSelectedSensor] = useState('');
    const [sensorStatus, setSensorStatus] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleSensorSelect = async (sensorType) => {
        setSelectedSensor(sensorType);
        setLoading(true);
        try {
            const status = await apiCall(`/api/auth/me/sensors/status/${sensorType}`);
            // Ensure the status object has the expected structure
            setSensorStatus({
                enabled: status?.enabled ?? false,
                connected: status?.connected ?? false,
                sensorType: sensorType
            });
        } catch (err) {
            console.error('Error fetching sensor status:', err);
            setSensorStatus({
                enabled: false,
                connected: false,
                sensorType: sensorType
            });
        } finally {
            setLoading(false);
        }
    };

    const handleSensorAction = async (action) => {
        if (!selectedSensor) return;

        setLoading(true);
        try {
            await apiCall(`/api/auth/me/sensors/${action}/${selectedSensor}`, 'POST');
            // Refresh status after action
            const status = await apiCall(`/api/auth/me/sensors/status/${selectedSensor}`);
            setSensorStatus(status);
        } catch (err) {
            console.error(`Error ${action} sensor:`, err);
        } finally {
            setLoading(false);
        }
    };

    const getSensorIcon = (type) => {
        switch(type) {
            case 'TEMPERATURE': return '🌡️';
            case 'HUMIDITY': return '💧';
            case 'MOTION': return '👀';
            case 'SOUND': return '🔊';
            case 'LIGHT': return '💡';
            default: return '📱';
        }
    };

    return (
        <div className="sensor-control-panel">
            <div className="sensor-selector">
                <select
                    value={selectedSensor}
                    onChange={(e) => handleSensorSelect(e.target.value)}
                    disabled={loading}
                >
                    <option value="">Select a sensor</option>
                    {sensorTypes?.map(type => (
                        <option key={type} value={type}>
                            {getSensorIcon(type)} {type}
                        </option>
                    ))}
                </select>
            </div>

            {selectedSensor && (
                <div className="sensor-status-card">
                    <div className="sensor-header">
                        <span className="sensor-icon">{getSensorIcon(selectedSensor)}</span>
                        <h3>{selectedSensor}</h3>
                    </div>

                    {loading ? (
                        <div className="loading-indicator">Loading...</div>
                    ) : (
                        <>
                            <div className="status-info">
                                <div className={`status-indicator ${sensorStatus?.connected ? 'connected' : 'disconnected'}`}>
                                    {sensorStatus?.connected ? '🟢 Connected' : '🔴 Disconnected'}
                                </div>
                                <div className={`status-indicator ${sensorStatus?.enabled ? 'enabled' : 'disabled'}`}>
                                    {sensorStatus?.enabled ? '✅ Enabled' : '❌ Disabled'}
                                </div>
                            </div>

                            <div className="sensor-actions">
                                <button
                                    onClick={() => handleSensorAction('enable')}
                                    disabled={loading || sensorStatus?.enabled}
                                    className="action-btn enable-btn"
                                >
                                    Enable
                                </button>
                                <button
                                    onClick={() => handleSensorAction('disable')}
                                    disabled={loading || !sensorStatus?.enabled}
                                    className="action-btn disable-btn"
                                >
                                    Disable
                                </button>
                                <button
                                    onClick={() => handleSensorAction('restart')}
                                    disabled={loading}
                                    className="action-btn restart-btn"
                                >
                                    Restart
                                </button>
                            </div>
                        </>
                    )}
                </div>
            )}
        </div>
    );
};

export default SensorControlPanel;