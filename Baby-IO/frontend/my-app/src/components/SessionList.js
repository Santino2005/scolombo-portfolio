// src/components/history/SessionList.js
import React from 'react';
import { Link } from 'react-router-dom';

const SessionList = ({ sessions, onDelete }) => {
    return (
        <div className="session-list">
            {sessions.length === 0 ? (
                <p>No sessions found</p>
            ) : (
                <table>
                    <thead>
                    <tr>
                        <th>Start Time</th>
                        <th>End Time</th>
                        <th>Duration</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {sessions.map((session) => (
                        <tr key={session.id}>
                            <td>{new Date(session.startTime).toLocaleString()}</td>
                            <td>{session.endTime ? new Date(session.endTime).toLocaleString() : '-'}</td>
                            <td>{session.duration || '-'}</td>
                            <td>{session.status}</td>
                            <td>
                                <Link to={`/session/${session.id}`}>View</Link>
                                <button onClick={() => onDelete(session.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default SessionList;