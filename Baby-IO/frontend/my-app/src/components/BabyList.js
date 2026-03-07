// BabyList.js
import React from 'react';
import '../styles/BabyList.css';
import { FaInfoCircle } from 'react-icons/fa';

const BabyList = ({ babies, onEdit, onDelete, onViewDetails, onSwitchBaby, onManageRoutines, currentBaby }) => {
    const showInfo = () => {
        alert("Biologically vulnerable babies are at higher risk for SIDS (Sudden Infant Death Syndrome). These infants require extra monitoring and care. Always follow safe sleep practices.");
    };

    return (
        <div className="baby-list-container">
            {babies.length === 0 ? (
                <div className="empty-state">
                    <p>No babies found</p>
                    <p>Add your first baby to get started!</p>
                </div>
            ) : (
                <div className="baby-table-container">
                    <table className="baby-table">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Age</th>
                            <th>Gender</th>
                            <th>Vulnerable</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {babies.map((baby) => (
                            <tr
                                key={baby.id}
                                className={`${currentBaby?.id === baby.id ? 'selected-baby' : ''} ${baby.isBioVulnerable ? 'vulnerable-baby' : ''}`}
                            >
                                <td>
                                    {baby.name}
                                    {currentBaby?.id === baby.id && (
                                        <span className="selected-badge">Current</span>
                                    )}
                                </td>
                                <td>{baby.ageInMonths} mo</td>
                                <td>{baby.gender.charAt(0).toUpperCase() + baby.gender.slice(1)}</td>
                                <td>{baby.isBioVulnerable ? 'Yes' : 'No'}</td>
                                <td className="actions">
                                    <button
                                        className="btn btn-info"
                                        onClick={() => onViewDetails(baby)}
                                    >
                                        Details
                                    </button>
                                    <button
                                        className="btn btn-warning"
                                        onClick={() => onEdit(baby)}
                                    >
                                        Edit
                                    </button>
                                    <button
                                        className="btn btn-danger"
                                        onClick={() => onDelete(baby.id)}
                                    >
                                        Delete
                                    </button>
                                    <button
                                        className="btn btn-routine"
                                        onClick={() => onManageRoutines(baby)}
                                    >
                                        Routines
                                    </button>
                                    {currentBaby?.id !== baby.id && (
                                        <button
                                            className="btn btn-primary"
                                            onClick={() => onSwitchBaby(baby)}
                                        >
                                            Switch To
                                        </button>
                                    )}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    <button
                        className="info-button"
                        onClick={showInfo}
                    >
                        <FaInfoCircle /> About SIDS & Bio Vulnerability
                    </button>
                </div>
            )}
        </div>
    );
};

export default BabyList;