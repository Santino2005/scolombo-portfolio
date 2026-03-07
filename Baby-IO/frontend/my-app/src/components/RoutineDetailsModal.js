import React, { useState } from 'react';
import '../styles/RoutineDetailsModal.css';

const RoutineDetailsModal = ({ routine, onClose, onEdit, onDelete }) => {
    const [activeTab, setActiveTab] = useState('general');

    if (!routine) return null;

    return (
        <div className="modal-overlay">
            <div className="routine-details-modal">
                <div className="modal-header">
                    <h3>{routine.name}</h3>
                    <button className="close-btn" onClick={onClose}>×</button>
                </div>

                <div className="tabs">
                    <button
                        className={`tab ${activeTab === 'general' ? 'active' : ''}`}
                        onClick={() => setActiveTab('general')}
                    >
                        General
                    </button>
                    <button
                        className={`tab ${activeTab === 'sensors' ? 'active' : ''}`}
                        onClick={() => setActiveTab('sensors')}
                    >
                        Sensors
                    </button>
                    <button
                        className={`tab ${activeTab === 'lullaby' ? 'active' : ''}`}
                        onClick={() => setActiveTab('lullaby')}
                    >
                        Lullaby Player
                    </button>
                </div>

                <div className="tab-content">
                    {activeTab === 'general' && (
                        <div className="general-section">
                            <div className="detail-row">
                                <span className="label">Description:</span>
                                <span>{routine.description || 'N/A'}</span>
                            </div>
                            <div className="detail-row">
                                <span className="label">Duration:</span>
                                <span>{routine.defaultDurationMinutes} minutes</span>
                            </div>
                            <div className="detail-row">
                                <span className="label">Alerts:</span>
                                <span>{routine.enableAlerts ? 'Enabled' : 'Disabled'}</span>
                            </div>
                            {routine.enableAlerts && (
                                <>
                                    <div className="detail-row">
                                        <span className="label">Medium Alert Timeout:</span>
                                        <span>{routine.mediumAlertTimeoutSeconds} seconds</span>
                                    </div>
                                    <div className="detail-row">
                                        <span className="label">High Alert Timeout:</span>
                                        <span>{routine.highAlertTimeoutSeconds} seconds</span>
                                    </div>
                                </>
                            )}
                        </div>
                    )}

                    {activeTab === 'sensors' && (
                        <div className="sensors-section">
                            <h4>Sensor Configurations</h4>
                            {routine.sensorConfigurations?.length > 0 ? (
                                <table className="sensor-table">
                                    <thead>
                                    <tr>
                                        <th>Sensor</th>
                                        <th>Enabled</th>
                                        <th>Logging</th>
                                        <th>Log Interval</th>
                                        {routine.enableAlerts && (
                                            <>
                                                <th>Medium Threshold</th>
                                                <th>High Threshold</th>
                                            </>
                                        )}
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {routine.sensorConfigurations.map((sensor) => (
                                        <tr key={sensor.sensorType}>
                                            <td>{sensor.sensorType}</td>
                                            <td>{sensor.enabled ? 'Yes' : 'No'}</td>
                                            <td>{sensor.loggingEnabled ? 'Yes' : 'No'}</td>
                                            <td>{sensor.loggingIntervalMinutes} mins</td>
                                            {routine.enableAlerts && (
                                                <>
                                                    <td>{sensor.mediumAlertThreshold}</td>
                                                    <td>{sensor.highAlertThreshold}</td>
                                                </>
                                            )}
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            ) : (
                                <p>No sensor configurations found</p>
                            )}
                        </div>
                    )}

                    {activeTab === 'lullaby' && (
                        <div className="lullaby-section">
                            <h4>Lullaby Player Configuration</h4>
                            {routine.lullabyPlayerConfiguration ? (
                                <div className="lullaby-details">
                                    <div className="detail-row">
                                        <span className="label">Enabled:</span>
                                        <span>{routine.lullabyPlayerConfiguration.enabled ? 'Yes' : 'No'}</span>
                                    </div>
                                    {routine.lullabyPlayerConfiguration.enabled && (
                                        <>
                                            <div className="detail-row">
                                                <span className="label">Volume:</span>
                                                <span>{routine.lullabyPlayerConfiguration.volume}</span>
                                            </div>
                                            <div className="detail-row">
                                                <span className="label">Alert Lullabies:</span>
                                                <span>{routine.lullabyPlayerConfiguration.alertLullabyEnabled || routine.lullabyPlayerConfiguration.enableAlertLullaby ? 'Enabled' : 'Disabled'}</span>
                                            </div>
                                            {(routine.lullabyPlayerConfiguration.alertLullabyEnabled || routine.lullabyPlayerConfiguration.enableAlertLullaby) && (
                                                <>
                                                    <div className="detail-row">
                                                        <span className="label">Medium Alert Lullaby:</span>
                                                        <span>{routine.lullabyPlayerConfiguration.mediumAlertLullabyId || 'Not set'}</span>
                                                    </div>
                                                    <div className="detail-row">
                                                        <span className="label">High Alert Lullaby:</span>
                                                        <span>{routine.lullabyPlayerConfiguration.highAlertLullabyId || 'Not set'}</span>
                                                    </div>
                                                </>
                                            )}
                                            <div className="detail-row">
                                                <span className="label">Periodic Lullaby:</span>
                                                <span>{routine.lullabyPlayerConfiguration.enablePeriodicLullaby ? 'Enabled' : 'Disabled'}</span>
                                            </div>
                                            {routine.lullabyPlayerConfiguration.enablePeriodicLullaby && (
                                                <>
                                                    <div className="detail-row">
                                                        <span className="label">Interval:</span>
                                                        <span>{routine.lullabyPlayerConfiguration.periodicLullabyIntervalMinutes} minutes</span>
                                                    </div>
                                                    <div className="detail-row">
                                                        <span className="label">Lullaby:</span>
                                                        <span>{routine.lullabyPlayerConfiguration.periodicLullabyId || 'Not set'}</span>
                                                    </div>
                                                </>
                                            )}
                                            <div className="detail-row">
                                                <span className="label">Wake Up Lullaby:</span>
                                                <span>{routine.lullabyPlayerConfiguration.enableWakeUpLullaby ? 'Enabled' : 'Disabled'}</span>
                                            </div>
                                            {routine.lullabyPlayerConfiguration.enableWakeUpLullaby && (
                                                <div className="detail-row">
                                                    <span className="label">Lullaby:</span>
                                                    <span>{routine.lullabyPlayerConfiguration.wakeUpLullabyId || 'Not set'}</span>
                                                </div>
                                            )}
                                        </>
                                    )}
                                </div>
                            ) : (
                                <p>Lullaby player configuration not found</p>
                            )}
                        </div>
                    )}
                </div>

                <div className="modal-actions">
                    <button className="btn-edit" onClick={() => onEdit(routine)}>
                        Edit Routine
                    </button>
                    <button className="btn-delete" onClick={() => onDelete(routine.id)}>
                        Delete Routine
                    </button>
                    <button className="btn-close" onClick={onClose}>
                        Close
                    </button>
                </div>
            </div>
        </div>
    );
};

export default RoutineDetailsModal;