import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiCall } from '../config/Api';
import SessionList from '../components/SessionList';
import '../styles/SleepHistory.css';

const SleepHistory = ({ currentBaby }) => {
    const navigate = useNavigate();
    const [savedSessions, setSavedSessions] = useState([]);
    const [discardedSessions, setDiscardedSessions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [activeTab, setActiveTab] = useState('saved');
    const [error, setError] = useState(null);

    useEffect(() => {
        if (currentBaby) {
            fetchSessions();
        }
    }, [currentBaby]);

    const fetchSessions = async () => {
        try {
            setLoading(true);
            setError(null);

            const [saved, discarded] = await Promise.all([
                apiCall('/api/auth/me/sleep/session/all/saved'),
                apiCall('/api/auth/me/sleep/session/all/discarded')
            ]);

            const filteredSaved = currentBaby ?
                saved.filter(session => session.baby?.id === currentBaby.id) :
                saved;

            const filteredDiscarded = currentBaby ?
                discarded.filter(session => session.baby?.id === currentBaby.id) :
                discarded;

            setSavedSessions(filteredSaved);
            setDiscardedSessions(filteredDiscarded);
        } catch (error) {
            console.error('Failed to fetch sessions:', error);
            setError(error.message || 'Failed to load sleep sessions');
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteSession = async (sessionId) => {
        try {
            await apiCall(`/api/auth/me/sleep/session/${sessionId}/delete`, 'DELETE');
            fetchSessions();
        } catch (error) {
            console.error('Failed to delete session:', error);
            setError('Failed to delete session');
        }
    };

    if (!currentBaby) {
        return (
            <div className="no-baby-selected">
                <p>Please select a baby to view sleep history</p>
                <button onClick={() => navigate('/babies')} className="btn-primary">
                    Select Baby
                </button>
            </div>
        );
    }

    if (loading) {
        return (
            <div className="loading-container">
                <div className="spinner"></div>
                <p>Loading sleep history...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="error-container">
                <h3>Error Loading Sleep History</h3>
                <p>{error}</p>
                <button onClick={fetchSessions} className="retry-button">
                    Try Again
                </button>
            </div>
        );
    }

    return (
        <div className="sleep-history">
            <div className="sleep-history-header">
                <h2>Sleep History for {currentBaby.name}</h2>
                <div className="baby-info">
                    <span>Age: {currentBaby.ageInMonths} months</span>
                    <span>Weight: {currentBaby.weightInKilograms} kg</span>
                    <span>Vulnerable: {currentBaby.isBioVulnerable ? 'Yes' : 'No'}</span>
                </div>
            </div>

            <div className="tabs">
                <button
                    className={activeTab === 'saved' ? 'active' : ''}
                    onClick={() => setActiveTab('saved')}
                >
                    Saved Sessions ({savedSessions.length})
                </button>
                <button
                    className={activeTab === 'discarded' ? 'active' : ''}
                    onClick={() => setActiveTab('discarded')}
                >
                    Discarded Sessions ({discardedSessions.length})
                </button>
            </div>

            {activeTab === 'saved' ? (
                <div className="sessions-section">
                    {savedSessions.length === 0 ? (
                        <div className="no-sessions">
                            <p>No saved sessions found for {currentBaby.name}</p>
                            <p>Complete sleep sessions and save them to see history here.</p>
                        </div>
                    ) : (
                        <SessionList
                            sessions={savedSessions}
                            onDelete={handleDeleteSession}
                        />
                    )}
                </div>
            ) : (
                <div className="sessions-section">
                    {discardedSessions.length === 0 ? (
                        <div className="no-sessions">
                            <p>No discarded sessions found for {currentBaby.name}</p>
                        </div>
                    ) : (
                        <SessionList
                            sessions={discardedSessions}
                            onDelete={handleDeleteSession}
                        />
                    )}
                </div>
            )}
        </div>
    );
};

export default SleepHistory;