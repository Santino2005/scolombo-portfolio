import React, { useState, useEffect } from 'react';
import '../styles/RoutineForm.css';

const RoutineForm = ({ routine, onSubmit, onCancel }) => {

    const validateForm = () => {
        // Validate lullaby player options if enabled
        if (formData.lullabyPlayerConfiguration.enabled) {
            const lullabyConfig = formData.lullabyPlayerConfiguration;
            if (!lullabyConfig.alertLullabyEnabled &&
                !lullabyConfig.enablePeriodicLullaby &&
                !lullabyConfig.enableWakeUpLullaby) {
                alert('Please select at least one lullaby option (Alert, Periodic, or Wake Up)');
                return false;
            }
        }

        // Validate sensor configurations
        for (const sensor of formData.sensorConfigurations) {
            if (sensor.enabled && !sensor.loggingEnabled && !formData.enableAlerts) {
                alert(`Sensor ${sensor.sensorType} is enabled but has no function. Please enable either logging or alerts.`);
                return false;
            }

            if (sensor.enabled && !sensor.loggingEnabled && formData.enableAlerts &&
                sensor.mediumAlertThreshold === 0 && sensor.highAlertThreshold === 0) {
                alert(`Sensor ${sensor.sensorType} is enabled but has no thresholds set. Please set alert thresholds or enable logging.`);
                return false;
            }
        }

        return true;
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        // Debug: Log the complete form data being submitted
        console.log('=== SUBMITTING FORM DATA ===');
        console.log('Form Data:', JSON.stringify(formData, null, 2));
        console.log('Sensor Configurations:');
        formData.sensorConfigurations.forEach((sensor, index) => {
            console.log(`  ${index}: ${sensor.sensorType} - Enabled: ${sensor.enabled}, LoggingEnabled: ${sensor.loggingEnabled}`);
        });
        console.log('========================');

        if (validateForm()) {
            onSubmit(formData);
        }
    };

    const [formData, setFormData] = useState({
        name: '',
        description: '',
        defaultDurationMinutes: 30,
        enableAlerts: false,
        mediumAlertTimeoutSeconds: 60,
        highAlertTimeoutSeconds: 120,
        lullabyPlayerConfiguration: {
            enabled: false,
            volume: 15,
            alertLullabyEnabled: false,
            mediumAlertLullabyId: null,
            highAlertLullabyId: null,
            enablePeriodicLullaby: false,
            periodicLullabyIntervalMinutes: 15,
            periodicLullabyId: null,
            enableWakeUpLullaby: false,
            wakeUpLullabyId: null
        },
        sensorConfigurations: [
            {
                sensorType: 'TEMPERATURE',
                enabled: true,
                loggingEnabled: true,
                loggingIntervalMinutes: 5,
                mediumAlertThreshold: 25,
                highAlertThreshold: 30
            },
            {
                sensorType: 'HUMIDITY',
                enabled: true,
                loggingEnabled: true,
                loggingIntervalMinutes: 5,
                mediumAlertThreshold: 40,
                highAlertThreshold: 50
            },
            {
                sensorType: 'SOUND',
                enabled: true,
                loggingEnabled: true,
                loggingIntervalMinutes: 5,
                mediumAlertThreshold: 70,
                highAlertThreshold: 85
            },
            {
                sensorType: 'MOTION',
                enabled: true,
                loggingEnabled: true,
                loggingIntervalMinutes: 5,
                mediumAlertThreshold: 50,
                highAlertThreshold: 70
            }
        ]
    });

    useEffect(() => {
        if (routine) {
            setFormData({
                name: routine.name || '',
                description: routine.description || '',
                defaultDurationMinutes: routine.defaultDurationMinutes || 30,
                enableAlerts: routine.enableAlerts || false,
                mediumAlertTimeoutSeconds: routine.mediumAlertTimeoutSeconds || 60,
                highAlertTimeoutSeconds: routine.highAlertTimeoutSeconds || 120,
                lullabyPlayerConfiguration: {
                    enabled: routine.lullabyPlayerConfiguration?.enabled || false,
                    volume: routine.lullabyPlayerConfiguration?.volume || 15,
                    alertLullabyEnabled: routine.lullabyPlayerConfiguration?.alertLullabyEnabled || false,
                    mediumAlertLullabyId: routine.lullabyPlayerConfiguration?.mediumAlertLullabyId || null,
                    highAlertLullabyId: routine.lullabyPlayerConfiguration?.highAlertLullabyId || null,
                    enablePeriodicLullaby: routine.lullabyPlayerConfiguration?.enablePeriodicLullaby || false,
                    periodicLullabyIntervalMinutes: routine.lullabyPlayerConfiguration?.periodicLullabyIntervalMinutes || 15,
                    periodicLullabyId: routine.lullabyPlayerConfiguration?.periodicLullabyId || null,
                    enableWakeUpLullaby: routine.lullabyPlayerConfiguration?.enableWakeUpLullaby || false,
                    wakeUpLullabyId: routine.lullabyPlayerConfiguration?.wakeUpLullabyId || null
                },
                sensorConfigurations: routine.sensorConfigurations || [
                    {
                        sensorType: 'TEMPERATURE',
                        enabled: true,
                        loggingEnabled: true,
                        loggingIntervalMinutes: 5,
                        mediumAlertThreshold: 25,
                        highAlertThreshold: 30
                    },
                    {
                        sensorType: 'HUMIDITY',
                        enabled: true,
                        loggingEnabled: true,
                        loggingIntervalMinutes: 5,
                        mediumAlertThreshold: 40,
                        highAlertThreshold: 50
                    },
                    {
                        sensorType: 'SOUND',
                        enabled: true,
                        loggingEnabled: true,
                        loggingIntervalMinutes: 5,
                        mediumAlertThreshold: 70,
                        highAlertThreshold: 85
                    },
                    {
                        sensorType: 'MOTION',
                        enabled: true,
                        loggingEnabled: true,
                        loggingIntervalMinutes: 5,
                        mediumAlertThreshold: 50,
                        highAlertThreshold: 70
                    }
                ]
            });
        }
    }, [routine]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : type === 'number' ? parseInt(value) || 0 : value
        }));
    };

    const handleLullabyChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            lullabyPlayerConfiguration: {
                ...prev.lullabyPlayerConfiguration,
                [name]: type === 'checkbox' ? checked : type === 'number' ? parseInt(value) || 0 : value
            }
        }));
    };

    const handleSensorChange = (index, field, value) => {
        const updatedSensors = [...formData.sensorConfigurations];
        updatedSensors[index] = {
            ...updatedSensors[index],
            [field]: field === 'mediumAlertThreshold' || field === 'highAlertThreshold' ? parseFloat(value) || 0 :
                field === 'loggingIntervalMinutes' ? parseInt(value) || 1 :
                    typeof value === 'boolean' ? value : // Keep boolean values as-is
                        value
        };
        setFormData({ ...formData, sensorConfigurations: updatedSensors });
    };

    const lullabyOptions = [
        { id: 1, name: 'Soft Rain' },
        { id: 2, name: 'Ocean Waves' },
        { id: 3, name: 'White Noise' },
        { id: 4, name: 'Lullaby Melody' }
    ];

    return (
        <div className="routine-form-overlay">
            <div className="routine-form-container">
                <div className="routine-form-header">
                    <h3>{routine ? 'Edit Sleep Routine' : 'Create New Sleep Routine'}</h3>
                    <button className="close-btn" onClick={onCancel}>×</button>
                </div>

                <form onSubmit={handleSubmit} className="routine-form">
                    {/* Basic Information Section */}
                    <div className="form-section">
                        <h4 className="section-title">Basic Information</h4>
                        <div className="form-group">
                            <label>Name*</label>
                            <input
                                type="text"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                required
                                className="form-input"
                            />
                        </div>
                        <div className="form-group">
                            <label>Description</label>
                            <textarea
                                name="description"
                                value={formData.description}
                                onChange={handleChange}
                                rows="3"
                                className="form-textarea"
                            />
                        </div>
                        <div className="form-group">
                            <label>Default Duration (minutes)*</label>
                            <input
                                type="number"
                                name="defaultDurationMinutes"
                                min="1"
                                value={formData.defaultDurationMinutes}
                                onChange={handleChange}
                                required
                                className="form-input"
                            />
                        </div>
                    </div>

                    {/* Alerts Section */}
                    <div className="form-section">
                        <h4 className="section-title">Alert Settings</h4>
                        <div className="checkbox-group">
                            <label className="checkbox-label">
                                <input
                                    type="checkbox"
                                    name="enableAlerts"
                                    checked={formData.enableAlerts}
                                    onChange={handleChange}
                                    className="form-checkbox"
                                />
                                <span>Enable Alerts</span>
                            </label>
                        </div>

                        <div className={`alert-settings ${!formData.enableAlerts ? 'disabled-section' : ''}`}>
                            <div className="form-group">
                                <label>Medium Alert Timeout (seconds)</label>
                                <input
                                    type="number"
                                    name="mediumAlertTimeoutSeconds"
                                    min="1"
                                    value={formData.mediumAlertTimeoutSeconds}
                                    onChange={handleChange}
                                    disabled={!formData.enableAlerts}
                                    className={`form-input ${!formData.enableAlerts ? 'disabled' : ''}`}
                                />
                            </div>
                            <div className="form-group">
                                <label>High Alert Timeout (seconds)</label>
                                <input
                                    type="number"
                                    name="highAlertTimeoutSeconds"
                                    min="1"
                                    value={formData.highAlertTimeoutSeconds}
                                    onChange={handleChange}
                                    disabled={!formData.enableAlerts}
                                    className={`form-input ${!formData.enableAlerts ? 'disabled' : ''}`}
                                />
                            </div>
                        </div>
                    </div>

                    {/* Lullaby Player Section */}
                    <div className="form-section">
                        <h4 className="section-title">Lullaby Player</h4>
                        <div className="checkbox-group">
                            <label className="checkbox-label">
                                <input
                                    type="checkbox"
                                    name="enabled"
                                    checked={formData.lullabyPlayerConfiguration.enabled}
                                    onChange={handleLullabyChange}
                                    className="form-checkbox"
                                />
                                <span>Enable Lullaby Player</span>
                            </label>
                        </div>

                        <div className={`lullaby-settings ${!formData.lullabyPlayerConfiguration.enabled ? 'disabled-section' : ''}`}>
                            <div className="form-group">
                                <label>Volume (0-30)</label>
                                <input
                                    type="number"
                                    name="volume"
                                    min="0"
                                    max="30"
                                    value={formData.lullabyPlayerConfiguration.volume}
                                    onChange={handleLullabyChange}
                                    disabled={!formData.lullabyPlayerConfiguration.enabled}
                                    className={`form-input ${!formData.lullabyPlayerConfiguration.enabled ? 'disabled' : ''}`}
                                />
                            </div>

                            <div className="validation-warning">
                                <small>* At least one lullaby option must be enabled when lullaby player is active</small>
                            </div>

                            <div className={`checkbox-group ${!formData.enableAlerts || !formData.lullabyPlayerConfiguration.enabled ? 'disabled-section' : ''}`}>
                                <label className="checkbox-label">
                                    <input
                                        type="checkbox"
                                        name="alertLullabyEnabled"
                                        checked={formData.lullabyPlayerConfiguration.alertLullabyEnabled}
                                        onChange={handleLullabyChange}
                                        disabled={!formData.enableAlerts || !formData.lullabyPlayerConfiguration.enabled}
                                        className={`form-checkbox ${!formData.enableAlerts || !formData.lullabyPlayerConfiguration.enabled ? 'disabled' : ''}`}
                                    />
                                    <span>Enable Alert Lullabies</span>
                                </label>
                            </div>

                            <div className={`form-group ${!formData.lullabyPlayerConfiguration.alertLullabyEnabled || !formData.enableAlerts || !formData.lullabyPlayerConfiguration.enabled ? 'disabled-section' : ''}`}>
                                <label>Medium Alert Lullaby</label>
                                <select
                                    name="mediumAlertLullabyId"
                                    value={formData.lullabyPlayerConfiguration.mediumAlertLullabyId || ''}
                                    onChange={handleLullabyChange}
                                    disabled={!formData.lullabyPlayerConfiguration.alertLullabyEnabled || !formData.enableAlerts || !formData.lullabyPlayerConfiguration.enabled}
                                    className={`form-select ${!formData.lullabyPlayerConfiguration.alertLullabyEnabled || !formData.enableAlerts || !formData.lullabyPlayerConfiguration.enabled ? 'disabled' : ''}`}
                                >
                                    <option value="">Select a lullaby...</option>
                                    {lullabyOptions.map(lullaby => (
                                        <option key={lullaby.id} value={lullaby.id}>
                                            {lullaby.name}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div className={`form-group ${!formData.lullabyPlayerConfiguration.alertLullabyEnabled || !formData.enableAlerts || !formData.lullabyPlayerConfiguration.enabled ? 'disabled-section' : ''}`}>
                                <label>High Alert Lullaby</label>
                                <select
                                    name="highAlertLullabyId"
                                    value={formData.lullabyPlayerConfiguration.highAlertLullabyId || ''}
                                    onChange={handleLullabyChange}
                                    disabled={!formData.lullabyPlayerConfiguration.alertLullabyEnabled || !formData.enableAlerts || !formData.lullabyPlayerConfiguration.enabled}
                                    className={`form-select ${!formData.lullabyPlayerConfiguration.alertLullabyEnabled || !formData.enableAlerts || !formData.lullabyPlayerConfiguration.enabled ? 'disabled' : ''}`}
                                >
                                    <option value="">Select a lullaby...</option>
                                    {lullabyOptions.map(lullaby => (
                                        <option key={lullaby.id} value={lullaby.id}>
                                            {lullaby.name}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div className={`checkbox-group ${!formData.lullabyPlayerConfiguration.enabled ? 'disabled-section' : ''}`}>
                                <label className="checkbox-label">
                                    <input
                                        type="checkbox"
                                        name="enablePeriodicLullaby"
                                        checked={formData.lullabyPlayerConfiguration.enablePeriodicLullaby}
                                        onChange={handleLullabyChange}
                                        disabled={!formData.lullabyPlayerConfiguration.enabled}
                                        className={`form-checkbox ${!formData.lullabyPlayerConfiguration.enabled ? 'disabled' : ''}`}
                                    />
                                    <span>Enable Periodic Lullaby</span>
                                </label>
                            </div>

                            <div className={`form-group ${!formData.lullabyPlayerConfiguration.enablePeriodicLullaby || !formData.lullabyPlayerConfiguration.enabled ? 'disabled-section' : ''}`}>
                                <label>Periodic Lullaby Interval (5-180 minutes)</label>
                                <input
                                    type="number"
                                    name="periodicLullabyIntervalMinutes"
                                    min="5"
                                    max="180"
                                    value={formData.lullabyPlayerConfiguration.periodicLullabyIntervalMinutes}
                                    onChange={handleLullabyChange}
                                    disabled={!formData.lullabyPlayerConfiguration.enablePeriodicLullaby || !formData.lullabyPlayerConfiguration.enabled}
                                    className={`form-input ${!formData.lullabyPlayerConfiguration.enablePeriodicLullaby || !formData.lullabyPlayerConfiguration.enabled ? 'disabled' : ''}`}
                                />
                            </div>

                            <div className={`form-group ${!formData.lullabyPlayerConfiguration.enablePeriodicLullaby || !formData.lullabyPlayerConfiguration.enabled ? 'disabled-section' : ''}`}>
                                <label>Periodic Lullaby</label>
                                <select
                                    name="periodicLullabyId"
                                    value={formData.lullabyPlayerConfiguration.periodicLullabyId || ''}
                                    onChange={handleLullabyChange}
                                    disabled={!formData.lullabyPlayerConfiguration.enablePeriodicLullaby || !formData.lullabyPlayerConfiguration.enabled}
                                    className={`form-select ${!formData.lullabyPlayerConfiguration.enablePeriodicLullaby || !formData.lullabyPlayerConfiguration.enabled ? 'disabled' : ''}`}
                                >
                                    <option value="">Select a lullaby...</option>
                                    {lullabyOptions.map(lullaby => (
                                        <option key={lullaby.id} value={lullaby.id}>
                                            {lullaby.name}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div className={`checkbox-group ${!formData.lullabyPlayerConfiguration.enabled ? 'disabled-section' : ''}`}>
                                <label className="checkbox-label">
                                    <input
                                        type="checkbox"
                                        name="enableWakeUpLullaby"
                                        checked={formData.lullabyPlayerConfiguration.enableWakeUpLullaby}
                                        onChange={handleLullabyChange}
                                        disabled={!formData.lullabyPlayerConfiguration.enabled}
                                        className={`form-checkbox ${!formData.lullabyPlayerConfiguration.enabled ? 'disabled' : ''}`}
                                    />
                                    <span>Enable Wake Up Lullaby</span>
                                </label>
                            </div>

                            <div className={`form-group ${!formData.lullabyPlayerConfiguration.enableWakeUpLullaby || !formData.lullabyPlayerConfiguration.enabled ? 'disabled-section' : ''}`}>
                                <label>Wake Up Lullaby</label>
                                <select
                                    name="wakeUpLullabyId"
                                    value={formData.lullabyPlayerConfiguration.wakeUpLullabyId || ''}
                                    onChange={handleLullabyChange}
                                    disabled={!formData.lullabyPlayerConfiguration.enableWakeUpLullaby || !formData.lullabyPlayerConfiguration.enabled}
                                    className={`form-select ${!formData.lullabyPlayerConfiguration.enableWakeUpLullaby || !formData.lullabyPlayerConfiguration.enabled ? 'disabled' : ''}`}
                                >
                                    <option value="">Select a lullaby...</option>
                                    {lullabyOptions.map(lullaby => (
                                        <option key={lullaby.id} value={lullaby.id}>
                                            {lullaby.name}
                                        </option>
                                    ))}
                                </select>
                            </div>
                        </div>
                    </div>

                    {/* Sensor Configurations Section */}
                    <div className="form-section">
                        <h4 className="section-title">Sensor Configurations</h4>
                        <div className="validation-warning">
                            <small>* Enabled sensors must have either logging enabled or alert thresholds set</small>
                        </div>
                        <div className="sensor-table-container">
                            <table className="sensor-config-table">
                                <thead>
                                <tr>
                                    <th>Sensor</th>
                                    <th>Enabled</th>
                                    <th>Logging</th>
                                    <th>Log Interval (min)</th>
                                    <th>Medium Alert Threshold</th>
                                    <th>High Alert Threshold</th>
                                </tr>
                                </thead>
                                <tbody>
                                {formData.sensorConfigurations.map((sensor, index) => (
                                    <tr key={sensor.sensorType} className={!sensor.enabled ? 'disabled-row' : ''}>
                                        <td>{sensor.sensorType}</td>
                                        <td>
                                            <label className="checkbox-label">
                                                <input
                                                    type="checkbox"
                                                    checked={sensor.enabled}
                                                    onChange={(e) => handleSensorChange(index, 'enabled', e.target.checked)}
                                                    className="form-checkbox"
                                                />
                                            </label>
                                        </td>
                                        <td>
                                            <label className="checkbox-label">
                                                <input
                                                    type="checkbox"
                                                    checked={sensor.loggingEnabled}
                                                    disabled={!sensor.enabled}
                                                    onChange={(e) => handleSensorChange(index, 'loggingEnabled', e.target.checked)}
                                                    className={`form-checkbox ${!sensor.enabled ? 'disabled' : ''}`}
                                                />
                                            </label>
                                        </td>
                                        <td>
                                            <input
                                                type="number"
                                                min="1"
                                                max="60"
                                                value={sensor.loggingIntervalMinutes}
                                                disabled={!sensor.enabled || !sensor.loggingEnabled}
                                                onChange={(e) => handleSensorChange(index, 'loggingIntervalMinutes', e.target.value)}
                                                className={`form-input ${!sensor.enabled || !sensor.loggingEnabled ? 'disabled' : ''}`}
                                            />
                                        </td>
                                        <td>
                                            <input
                                                type="number"
                                                step="0.1"
                                                min="0"
                                                max="100"
                                                value={sensor.mediumAlertThreshold}
                                                disabled={!sensor.enabled || !formData.enableAlerts}
                                                onChange={(e) => handleSensorChange(index, 'mediumAlertThreshold', e.target.value)}
                                                className={`form-input ${!sensor.enabled || !formData.enableAlerts ? 'disabled' : ''}`}
                                            />
                                        </td>
                                        <td>
                                            <input
                                                type="number"
                                                step="0.1"
                                                min="0"
                                                max="100"
                                                value={sensor.highAlertThreshold}
                                                disabled={!sensor.enabled || !formData.enableAlerts}
                                                onChange={(e) => handleSensorChange(index, 'highAlertThreshold', e.target.value)}
                                                className={`form-input ${!sensor.enabled || !formData.enableAlerts ? 'disabled' : ''}`}
                                            />
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div className="form-actions">
                        <button type="submit" className="btn btn-primary">
                            {routine ? 'Update Routine' : 'Create Routine'}
                        </button>
                        <button type="button" className="btn btn-secondary" onClick={onCancel}>
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default RoutineForm;