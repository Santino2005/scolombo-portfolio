import React, {useCallback, useEffect, useRef, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {apiCall} from '../config/Api';
import SessionControls from '../components/SessionControls';
import SessionStatus from '../components/SessionStatus';
import SensorData from '../components/SensorData';
import '../styles/SleepSession.css';

const SleepSession = ({ currentBaby, activeSession, setActiveSession, setCurrentBaby }) => {
    const [routines, setRoutines] = useState([]);
    const [sensorData, setSensorData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentAlert, setCurrentAlert] = useState(null);
    const [alertHistory, setAlertHistory] = useState([]);
    const [wsConnected, setWsConnected] = useState(false);
    const [showSaveDialog, setShowSaveDialog] = useState(false);
    const navigate = useNavigate();
    const mountedRef = useRef(true);
    const wsRef = useRef(null);

    const safeSetLoading = (value) => {
        if (mountedRef.current) {
            setLoading(value);
        }
    };

    const handleSleepEvent = useCallback((sleepEvent) => {
        if (!mountedRef.current || !sleepEvent.eventType) return;

        console.log('Received sleep event:', sleepEvent);

        switch (sleepEvent.eventType) {
            case 'SENSOR_ALERT_TRIGGERED':
                setCurrentAlert({
                    ...sleepEvent,
                    timestamp: new Date().toISOString() // Ensure timestamp is set
                });
                setAlertHistory(prev => [sleepEvent, ...prev.slice(0, 9)]);
                break;

            case 'USER_ATTENDED':
                setCurrentAlert(null);
                break;

            case 'SESSION_PAUSED':
                setActiveSession(prev => prev ? { ...prev, status: 'PAUSED' } : null);
                break;

            case 'SESSION_RESUMED':
                setActiveSession(prev => prev ? { ...prev, status: 'ACTIVE' } : null);
                break;

            case 'SESSION_ENDED':
            case 'SESSION_TERMINATED':
                setActiveSession(null);
                setCurrentAlert(null);
                setShowSaveDialog(true);
                break;

            default:
                console.log('Unhandled sleep event type:', sleepEvent.eventType);
        }
    }, [setActiveSession]);

    const connectWebSocket = useCallback(() => {
        if (wsRef.current?.readyState === WebSocket.OPEN) {
            return;
        }

        try {
            const wsProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
            const wsUrl = `${wsProtocol}//${window.location.host}/ws/sleep-events`;

            wsRef.current = new WebSocket(wsUrl);

            wsRef.current.onopen = () => {
                console.log('WebSocket connected');
                setWsConnected(true);
            };

            wsRef.current.onmessage = (event) => {
                try {
                    const message = JSON.parse(event.data);
                    if (message.type === 'sleep_event' && message.data) {
                        handleSleepEvent(message.data);
                    } else if (message.type === 'connection') {
                        console.log('WebSocket connection confirmed:', message.message);
                    }
                } catch (error) {
                    console.error('Error parsing WebSocket message:', error);
                }
            };

            wsRef.current.onclose = (event) => {
                console.log('WebSocket disconnected:', event.code, event.reason);
                setWsConnected(false);
                if (!window.location.hostname.includes('localhost')) {
                    setTimeout(() => {
                        if (mountedRef.current) {
                            connectWebSocket();
                        }
                    }, 3000);
                }
            };

            wsRef.current.onerror = (error) => {
                console.error('WebSocket error:', error);
                setWsConnected(false);
            };
        } catch (error) {
            console.error('Failed to create WebSocket connection:', error);
            setWsConnected(false);
        }
    }, [handleSleepEvent]);

    const fetchAvailableRoutines = useCallback(async () => {
        try {
            if (!currentBaby?.id) return;

            const data = await apiCall(`/api/auth/me/babies/${currentBaby.id}/sleep-routines`);
            if (mountedRef.current) {
                setRoutines(data || []);
            }
        } catch (error) {
            console.error('Failed to fetch routines:', error);
            if (mountedRef.current) {
                setError('Failed to load available routines');
            }
        }
    }, [currentBaby]);

    const fetchSensorData = useCallback(async () => {
        if (!activeSession?.id) return;

        try {
            const data = await apiCall('/api/auth/me/sleep/session/current/sensors/values');
            setSensorData(data || []);
        } catch (error) {
            console.error('Failed to fetch sensor data:', error);
        }
    }, [activeSession]);

    const checkExistingSession = useCallback(async () => {
        try {
            setError(null);
            const session = await apiCall('/api/auth/me/sleep/session/current');

            if (!mountedRef.current) return;

            if (session) {
                // Verify the session belongs to current baby
                if (!currentBaby || session.baby?.id !== currentBaby.id) {
                    console.warn('Session belongs to different baby, stopping it');
                    try {
                        await apiCall('/api/auth/me/sleep/session/current/stop', 'POST');
                    } catch (stopError) {
                        console.error('Failed to stop session:', stopError);
                    }
                    setActiveSession(null);
                    return;
                }
                setActiveSession(session);
            } else {
                setActiveSession(null);
            }
            safeSetLoading(false);
        } catch (error) {
            console.error('Failed to check active session:', error);
            if (mountedRef.current) {
                setError(error.response?.data?.message || error.message || 'Failed to check active session');
                safeSetLoading(false);
                setActiveSession(null);
            }
        }
    }, [currentBaby, setActiveSession]);

    useEffect(() => {
        return () => {
            mountedRef.current = false;
            if (wsRef.current) {
                wsRef.current.close();
            }
        };
    }, []);

    useEffect(() => {
        const checkBabyAndSession = async () => {
            if (!currentBaby?.id) {
                try {
                    const selectedBaby = await apiCall('/api/auth/me/babies/selected');
                    if (selectedBaby) {
                        setCurrentBaby(selectedBaby);
                    } else {
                        navigate('/babies');
                        return;
                    }
                } catch (error) {
                    console.error('Failed to fetch selected baby:', error);
                    navigate('/babies');
                    return;
                }
            }

            fetchAvailableRoutines();

            if (activeSession) {
                setLoading(false);
            } else {
                checkExistingSession();
            }
        };

        checkBabyAndSession();
        connectWebSocket();

        const sensorInterval = setInterval(fetchSensorData, 5000);

        return () => {
            clearInterval(sensorInterval);
        };
    }, [currentBaby, activeSession, navigate, setCurrentBaby, fetchAvailableRoutines, checkExistingSession, fetchSensorData, connectWebSocket]);

    const handleStartSession = async (routineId) => {
        if (!currentBaby?.id) return;

        try {
            safeSetLoading(true);
            setError(null);

            // Let the backend handle validation
            await apiCall('/api/auth/me/sleep/session/start', 'POST', {
                sleepRoutineId: parseInt(routineId), // Ensure it's a number
                babyId: currentBaby.id
            });

            // If successful, fetch the current session
            const session = await apiCall('/api/auth/me/sleep/session/current');
            if (session) {
                setActiveSession(session);
            }

            // Refresh routines after successful start
            await fetchAvailableRoutines();

        } catch (error) {
            console.error('Failed to start session:', error);
            if (mountedRef.current) {
                // The backend will return appropriate error messages
                setError(error.response?.data || error.message || 'Failed to start session');
            }
        } finally {
            if (mountedRef.current) {
                safeSetLoading(false);
            }
        }
    };

    const handleStopSession = async () => {
        try {
            setLoading(true);
            setError(null);

            await apiCall('/api/auth/me/sleep/session/current/stop', 'POST');
            setActiveSession(null);
            setCurrentAlert(null);
            setAlertHistory([]);
            setSensorData([]);
            setShowSaveDialog(true);
        } catch (error) {
            console.error('Failed to stop session:', error);
            setError(error.response?.data || error.message || 'Failed to stop session');
        } finally {
            setLoading(false);
        }
    };

    const handlePauseSession = async () => {
        try {
            setLoading(true);
            setError(null);

            await apiCall('/api/auth/me/sleep/session/current/pause', 'POST');
            setActiveSession(prev => prev ? { ...prev, status: 'PAUSED' } : null);
        } catch (error) {
            console.error('Failed to pause session:', error);
            setError(error.response?.data || error.message || 'Failed to pause session');
        } finally {
            setLoading(false);
        }
    };

    const handleResumeSession = async () => {
        try {
            setLoading(true);
            setError(null);

            await apiCall('/api/auth/me/sleep/session/current/resume', 'POST');
            setActiveSession(prev => prev ? { ...prev, status: 'ACTIVE' } : null);
        } catch (error) {
            console.error('Failed to resume session:', error);
            setError(error.response?.data || error.message || 'Failed to resume session');
        } finally {
            setLoading(false);
        }
    };

    const handleAttendAlert = async () => {
        try {
            setError(null);
            await apiCall('/api/auth/me/sleep/session/current/attend-alert', 'POST');
            setCurrentAlert(null);
        } catch (error) {
            console.error('Failed to attend alert:', error);
            setError(error.response?.data || error.message || 'Failed to attend alert');
        }
    };

    const handleSaveSession = async () => {
        try {
            setLoading(true);
            await apiCall(`/api/auth/me/sleep/session/${activeSession.id}/save`, 'POST');
            setShowSaveDialog(false);
        } catch (error) {
            console.error('Failed to save session:', error);
            setError('Failed to save session');
        } finally {
            setLoading(false);
        }
    };

    const handleDiscardSession = () => {
        setShowSaveDialog(false);
    };

    const formatTime = (dateTime) => {
        if (!dateTime) return 'N/A';
        return new Date(dateTime).toLocaleTimeString();
    };

    const getAlertSeverityClass = (alertLevel) => {
        switch (alertLevel?.toLowerCase()) {
            case 'critical': return 'alert-critical';
            case 'high': return 'alert-high';
            case 'medium': return 'alert-medium';
            case 'low': return 'alert-low';
            default: return 'alert-info';
        }
    };

    if (loading) {
        return (
            <div className="loading-container">
                <div className="spinner"></div>
                <p>Loading session data...</p>
            </div>
        );
    }

    if (!currentBaby) {
        return (
            <div className="no-baby-selected">
                <p>No baby selected. Please select a baby first.</p>
                <button onClick={() => navigate('/babies')}>Select Baby</button>
            </div>
        );
    }

    return (
        <div className={`sleep-session ${currentAlert ? 'alert-active' : ''}`}>
            <div className="session-header">
                <div className="session-title">
                    <h2>Sleep Session</h2>
                    {currentBaby?.name && (
                        <p className="baby-name">{currentBaby.name}</p>
                    )}
                </div>
                <div className="connection-status">
                    <span className={`ws-status ${wsConnected ? 'connected' : 'disconnected'}`}>
                        {wsConnected ? '🟢 Connected' : '🔴 Offline'}
                    </span>
                </div>
            </div>

            {error && (
                <div className="alert alert-error">
                    {error}
                </div>
            )}

            {currentAlert && (
                <div className={`current-alert ${getAlertSeverityClass(currentAlert.alertLevel)}`}>
                    <div className="alert-header">
                        <h3>🚨 Alert: {currentAlert.alertLevel?.toUpperCase() || 'UNKNOWN'}</h3>
                        <div className="alert-timestamp">
                            {formatTime(currentAlert.timestamp)}
                        </div>
                    </div>
                    <div className="alert-content">
                        <p className="alert-description">
                            {currentAlert.description || 'Sensor alert triggered'}
                        </p>
                        {currentAlert.triggerSensorType && (
                            <div className="alert-details">
                                <span>Sensor: {currentAlert.triggerSensorType}</span>
                                {currentAlert.sensorValue && (
                                    <span>Value: {currentAlert.sensorValue}</span>
                                )}
                                {currentAlert.thresholdValue && (
                                    <span>Threshold: {currentAlert.thresholdValue}</span>
                                )}
                            </div>
                        )}
                    </div>
                    <button
                        className="attend-alert-btn"
                        onClick={handleAttendAlert}
                        disabled={loading}
                    >
                        Attend Alert
                    </button>
                </div>
            )}

            {!activeSession ? (
                <SessionControls
                    routines={routines}
                    onStart={handleStartSession}
                    loading={loading}
                />
            ) : (
                <div className="session-content">
                    <SessionStatus session={activeSession} currentBaby={currentBaby} />

                    <div className="active-session-controls">
                        {activeSession && activeSession.status === 'ACTIVE' && (
                            <>
                                <button
                                    className="session-btn pause-btn"
                                    onClick={handlePauseSession}
                                    disabled={loading}
                                >
                                    ⏸️ Pause
                                </button>
                                <button
                                    className="session-btn stop-btn"
                                    onClick={handleStopSession}
                                    disabled={loading}
                                >
                                    ⏹️ Stop
                                </button>
                            </>
                        )}
                        {activeSession && activeSession.status === 'PAUSED' && (
                            <>
                                <button
                                    className="session-btn resume-btn"
                                    onClick={handleResumeSession}
                                    disabled={loading}
                                >
                                    ▶️ Resume
                                </button>
                                <button
                                    className="session-btn stop-btn"
                                    onClick={handleStopSession}
                                    disabled={loading}
                                >
                                    ⏹️ Stop
                                </button>
                            </>
                        )}
                        {currentAlert && (
                            <button
                                className="session-btn alert-btn"
                                onClick={handleAttendAlert}
                                disabled={loading}
                            >
                                🔔 Attend Alert
                            </button>
                        )}
                    </div>

                    <SensorData data={sensorData}/>

                    {alertHistory.length > 0 && (
                        <div className="alert-history">
                            <h4>Recent Alerts</h4>
                            <div className="alert-list">
                                {alertHistory.slice(0, 5).map((alert, index) => (
                                    <div key={index}
                                         className={`alert-item ${getAlertSeverityClass(alert.alertLevel)}`}>
                                        <div className="alert-item-header">
                                            <span className="alert-level">{alert.alertLevel}</span>
                                            <span className="alert-time">{formatTime(alert.timestamp)}</span>
                                        </div>
                                        <div className="alert-item-description">
                                            {alert.description || `${alert.triggerSensorType} alert`}
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}
                </div>
            )}

            {showSaveDialog && (
                <div className="save-session-dialog">
                    <div className="dialog-content">
                        <h3>Session Completed</h3>
                        <p>Would you like to save this sleep session?</p>
                        <div className="dialog-actions">
                            <button
                                className="btn-primary"
                                onClick={handleSaveSession}
                                disabled={loading}
                            >
                                Save Session
                            </button>
                            <button
                                className="btn-secondary"
                                onClick={handleDiscardSession}
                                disabled={loading}
                            >
                                Discard
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default SleepSession;