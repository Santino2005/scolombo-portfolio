import React from 'react';

const AlertNotification = ({ alert, onAttend }) => {
    if (!alert) return null;

    const alertClass = `alert-notification ${alert.alertLevel.toLowerCase()}`;

    return (
        <div className={alertClass}>
            <h3>Alert: {alert.eventType}</h3>
            <p>{alert.description}</p>
            {alert.triggerSensorType && (
                <p>Sensor: {alert.triggerSensorType} - Value: {alert.sensorValue}</p>
            )}
            <button onClick={onAttend} className="btn btn-primary">
                Attend Alert
            </button>
        </div>
    );
};

export default AlertNotification;