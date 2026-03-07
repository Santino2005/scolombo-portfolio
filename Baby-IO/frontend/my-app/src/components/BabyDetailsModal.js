// BabyDetailsModal.js
import React from 'react';
import '../styles/BabyDetailsModal.css';

const BabyDetailsModal = ({
                              baby,
                              details,
                              onClose
                          }) => {
    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <div className="modal-header">
                    <h3>{baby.name}'s Details</h3>
                    <button className="close-btn" onClick={onClose}>×</button>
                </div>

                <div className="details-section">
                    <h4>Basic Information</h4>
                    <div className="info-grid">
                        <div>
                            <label>Age</label>
                            <p>{baby.ageInMonths} months</p>
                        </div>
                        <div>
                            <label>Weight</label>
                            <p>{baby.weightInKilograms} kg</p>
                        </div>
                        <div>
                            <label>Gender</label>
                            <p>{baby.gender.charAt(0).toUpperCase() + baby.gender.slice(1)}</p>
                        </div>
                        <div>
                            <label>Vulnerable</label>
                            <p>{baby.isBioVulnerable ? 'Yes' : 'No'}</p>
                        </div>
                    </div>

                    <div className="medical-notes-section">
                        <h4>Medical Notes</h4>
                        <div className="medical-notes-content">
                            {baby.medicalNotes ? (
                                <p>{baby.medicalNotes}</p>
                            ) : (
                                <p className="no-notes">No medical notes provided</p>
                            )}
                        </div>
                    </div>
                </div>

                <div className="modal-footer">
                    <button className="btn-secondary" onClick={onClose}>
                        Close
                    </button>
                </div>
            </div>
        </div>
    );
};

export default BabyDetailsModal;