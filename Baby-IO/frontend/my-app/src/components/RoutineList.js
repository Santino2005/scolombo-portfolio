import React, { useState } from 'react';
import '../styles/RoutineList.css';
import RoutineDetailsModal from './RoutineDetailsModal';

const RoutineList = ({ routines, onEdit, onDelete, onAssign, showAssignButton }) => {
    const [selectedRoutine, setSelectedRoutine] = useState(null);
    const [showDetails, setShowDetails] = useState(false);

    const handleViewDetails = (routine) => {
        setSelectedRoutine(routine);
        setShowDetails(true);
    };

    return (
        <div className="routine-list-container">
            {routines.length === 0 ? (
                <div className="no-routines">
                    <p>No routines found.</p>
                </div>
            ) : (
                <div className="table-responsive">
                    <table className="routine-table">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Duration</th>
                            <th>Alerts</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {routines.map((routine) => (
                            <tr key={routine.id}>
                                <td>{routine.name}</td>
                                <td>{routine.description || '-'}</td>
                                <td>{routine.defaultDurationMinutes} mins</td>
                                <td>
                                    <span className={`alert-status ${routine.enableAlerts ? 'enabled' : 'disabled'}`}>
                                        {routine.enableAlerts ? 'Enabled' : 'Disabled'}
                                    </span>
                                </td>
                                <td className="actions">
                                    <button
                                        className="btn-view"
                                        onClick={() => handleViewDetails(routine)}
                                    >
                                        Details
                                    </button>
                                    <button
                                        className="btn-edit"
                                        onClick={() => onEdit(routine)}
                                    >
                                        Edit
                                    </button>
                                    <button
                                        className="btn-delete"
                                        onClick={() => onDelete(routine.id)}
                                    >
                                        Delete
                                    </button>
                                    {showAssignButton && (
                                        <button
                                            className="btn-assign"
                                            onClick={() => onAssign(routine.id)}
                                        >
                                            Assign
                                        </button>
                                    )}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}

            {showDetails && (
                <RoutineDetailsModal
                    routine={selectedRoutine}
                    onClose={() => setShowDetails(false)}
                    onEdit={onEdit}
                    onDelete={onDelete}
                />
            )}
        </div>
    );
};

export default RoutineList;