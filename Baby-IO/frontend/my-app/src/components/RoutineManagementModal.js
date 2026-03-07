// components/RoutineManagementModal.js
import React from 'react';
import '../styles/RoutineManagementModal.css';

const RoutineManagementModal = ({
                                    baby,
                                    routines,
                                    onClose,
                                    onRemove,
                                    onManageAllRoutines
                                }) => {
    const handleRemoveRoutine = (routine) => {
        // Log for debugging
        console.log('Removing routine:', routine);
        console.log('Routine ID:', routine.id);

        // Ensure we have a valid routine ID
        if (!routine || !routine.id) {
            console.error('Invalid routine object or missing ID:', routine);
            return;
        }

        // Call the parent handler with the routine ID
        onRemove(routine.id);
    };

    return (
        <div className="routine-modal-overlay">
            <div className="routine-modal-content">
                <div className="routine-modal-header">
                    <h3>Manage Sleep Routines for {baby.name}</h3>
                    <button className="close-btn" onClick={onClose}>×</button>
                </div>

                <div className="routine-list-section">
                    <h4>Assigned Routines</h4>
                    {routines.length > 0 ? (
                        <ul className="routine-list">
                            {routines.map(routine => (
                                <li key={routine.id} className="routine-item">
                                    <div>
                                        <h5>{routine.name}</h5>
                                        <p>{routine.description}</p>
                                    </div>
                                    <button
                                        className="btn-danger btn-sm"
                                        onClick={() => handleRemoveRoutine(routine)}
                                    >
                                        Remove
                                    </button>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p className="no-routines">No routines assigned</p>
                    )}
                </div>

                <div className="routine-modal-footer">
                    <button
                        className="btn-primary"
                        onClick={onManageAllRoutines}
                    >
                        Manage All Routines
                    </button>
                    <button
                        className="btn-secondary"
                        onClick={onClose}
                    >
                        Close
                    </button>
                </div>
            </div>
        </div>
    );
};

export default RoutineManagementModal;