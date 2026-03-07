import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiCall } from '../config/Api';
import BabyForm from '../components/BabyForm';
import '../styles/BabySelection.css';

const BabySelection = ({ currentBaby, onSelect, activeSession, redirectPath }) => {
    const [babies, setBabies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showForm, setShowForm] = useState(false);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const fetchBabies = useCallback(async () => {
        try {
            setLoading(true);
            const data = await apiCall('/api/auth/me/babies/list');
            setBabies(data);
            setError(null);
        } catch (error) {
            console.error('Failed to fetch babies:', error);
            setError('Failed to load babies. Please try again.');
        } finally {
            setLoading(false);
        }
    }, []);

    const fetchSelectedBaby = useCallback(async () => {
        try {
            const baby = await apiCall('/api/auth/me/babies/selected');
            if (baby) {
                onSelect(baby);
            }
        } catch (error) {
            if (error.response?.status !== 404) {
                console.error('Failed to fetch selected baby:', error);
            }
        }
    }, [onSelect]);

    useEffect(() => {
        fetchBabies();
        fetchSelectedBaby();
    }, [fetchBabies, fetchSelectedBaby]);

    const handleAddBaby = () => {
        setShowForm(true);
    };

    // Update the handleFormSubmit function to properly handle selection
    const handleFormSubmit = async (babyData) => {
        try {
            const baby = await apiCall('/api/auth/me/babies', 'POST', babyData);
            setShowForm(false);

            // Automatically select the newly created baby
            const selectedBaby = await apiCall(`/api/auth/me/babies/${baby.id}/select`, 'POST');
            await handleSelectBaby(selectedBaby);
        } catch (error) {
            console.error('Failed to save baby:', error);
            setError('Failed to save baby. Please try again.');
        }
    };

    const handleSelectBaby = async (baby) => {
        if (activeSession && activeSession.baby && activeSession.baby.id !== baby.id) {
            if (!window.confirm('You have an active sleep session for another baby. Do you want to stop it and switch babies?')) {
                return;
            }
        }

        try {
            // Call the backend to select the baby
            const selectedBaby = await apiCall(`/api/auth/me/babies/${baby.id}/select`, 'POST');
            onSelect(selectedBaby);

            if (redirectPath) {
                navigate(redirectPath);
            } else {
                navigate('/');
            }
        } catch (error) {
            console.error('Failed to select baby:', error);
            setError('Failed to select baby. Please try again.');
        }
    };

    if (loading) {
        return (
            <div className="loading-container">
                <div className="spinner"></div>
                <p>Loading babies...</p>
            </div>
        );
    }

    return (
        <div className="baby-selection-container">
            <div className="baby-selection-header">
                <h2>Select a Baby</h2>
                {activeSession && (
                    <div className="active-session-warning">
                        <p>You have an active sleep session for {activeSession.baby.name}</p>
                    </div>
                )}
                <button className="btn-primary" onClick={handleAddBaby}>
                    + Add New Baby
                </button>
            </div>

            {error && (
                <div className="alert error">
                    {error}
                    <button className="btn-text" onClick={fetchBabies}>Retry</button>
                </div>
            )}

            {showForm && (
                <BabyForm
                    onSubmit={handleFormSubmit}
                    onCancel={() => setShowForm(false)}
                />
            )}

            {babies.length > 0 ? (
                <div className="baby-list">
                    {babies.map(baby => (
                        <div
                            key={baby.id}
                            className={`baby-card ${currentBaby?.id === baby.id ? 'selected' : ''}`}
                            onClick={() => handleSelectBaby(baby)}
                        >
                            <div className="baby-card-header">
                                <h3>{baby.name}</h3>
                                {activeSession?.baby?.id === baby.id && (
                                    <div className="active-session-badge">Active Session</div>
                                )}
                            </div>
                            <div className="baby-details">
                                <div className="detail-item">
                                    <span className="detail-label">Age:</span>
                                    <span>{baby.ageInMonths} months</span>
                                </div>
                                <div className="detail-item">
                                    <span className="detail-label">Weight:</span>
                                    <span>{baby.weightInKilograms} kg</span>
                                </div>
                                <div className="detail-item">
                                    <span className="detail-label">Gender:</span>
                                    <span>{baby.gender}</span>
                                </div>
                                <div className="detail-item">
                                    <span className="detail-label">Vulnerable:</span>
                                    <span>{baby.isBioVulnerable ? 'Yes' : 'No'}</span>
                                </div>
                                {baby.medicalNotes && (
                                    <div className="detail-item notes">
                                        <span className="detail-label">Medical Notes:</span>
                                        <span>{baby.medicalNotes}</span>
                                    </div>
                                )}
                            </div>
                            {currentBaby?.id !== baby.id && (
                                <button
                                    className="switch-button"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        handleSelectBaby(baby);
                                    }}
                                >
                                    Switch
                                </button>
                            )}
                        </div>
                    ))}
                </div>
            ) : (
                <div className="empty-state">
                    <p>No babies found</p>
                    <p>Add your first baby to get started!</p>
                </div>
            )}
        </div>
    );
};

export default BabySelection;