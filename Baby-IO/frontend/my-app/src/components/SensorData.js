// src/components/session/SensorData.js
import React from 'react';

const SensorData = ({ data }) => {
    if (!data || data.length === 0) {
        return <div className="sensor-data">No sensor data available</div>;
    }

    return (
        <div className="sensor-data">
            <h3>Sensor Readings</h3>
            <table>
                <thead>
                <tr>
                    <th>Sensor Type</th>
                    <th>Value</th>
                    <th>Timestamp</th>
                </tr>
                </thead>
                <tbody>
                {data.map((reading, index) => (
                    <tr key={index}>
                        <td>{reading.sensorType}</td>
                        <td>{reading.value}</td>
                        <td>{new Date(reading.timestamp).toLocaleString()}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default SensorData;