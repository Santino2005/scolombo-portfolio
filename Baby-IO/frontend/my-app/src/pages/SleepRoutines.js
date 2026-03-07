import React, { useState, useEffect } from 'react';
import { apiCall } from '../config/Api';
import RoutineList from '../components/RoutineList';
import RoutineForm from '../components/RoutineForm';
import BabySelectionModal from '../components/BabySelectionModal';
import '../styles/SleepRoutines.css';

const SleepRoutines = () => {
    const [routines, setRoutines] = useState([]);
    const [babies, setBabies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showForm, setShowForm] = useState(false);
    const [showBabyModal, setShowBabyModal] = useState(false);
    const [currentRoutine, setCurrentRoutine] = useState(null);
    const [selectedRoutineId, setSelectedRoutineId] = useState(null);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            setLoading(true);
            const [routinesData, babiesData] = await Promise.all([
                apiCall('/api/auth/me/sleep/routines'),
                apiCall('/api/auth/me/babies/list')
            ]);

            setRoutines(routinesData);
            setBabies(babiesData);
            setLoading(false);
        } catch (error) {
            console.error('Failed to fetch data:', error);
            setLoading(false);
        }
    };

    const handleAddRoutine = () => {
        setCurrentRoutine(null);
        setShowForm(true);
    };

    const handleEditRoutine = (routine) => {
        setCurrentRoutine(routine);
        setShowForm(true);
    };

    const handleDeleteRoutine = async (routineId) => {
        if (window.confirm('Are you sure you want to delete this routine?')) {
            try {
                await apiCall(`/api/auth/me/sleep/routines/delete/${routineId}`, 'DELETE');
                fetchData();
            } catch (error) {
                console.error('Failed to delete routine:', error);
            }
        }
    };

    const handleFormSubmit = async (routineData) => {
        try {
            // Prepare the data for both creation and update
            const dataToSend = {
                name: routineData.name,
                description: routineData.description,
                defaultDurationMinutes: routineData.defaultDurationMinutes,
                enableAlerts: routineData.enableAlerts,
                mediumAlertTimeoutSeconds: routineData.mediumAlertTimeoutSeconds,
                highAlertTimeoutSeconds: routineData.highAlertTimeoutSeconds,
                sensorConfigurations: routineData.sensorConfigurations.map(sensor => ({
                    sensorType: sensor.sensorType,
                    enabled: sensor.enabled,
                    loggingEnabled: sensor.loggingEnabled,
                    loggingIntervalMinutes: sensor.loggingIntervalMinutes,
                    mediumAlertThreshold: sensor.mediumAlertThreshold,
                    highAlertThreshold: sensor.highAlertThreshold
                })),
                lullabyPlayerConfiguration: {
                    enabled: routineData.lullabyPlayerConfiguration.enabled,
                    volume: routineData.lullabyPlayerConfiguration.volume,
                    alertLullabyEnabled: routineData.lullabyPlayerConfiguration.alertLullabyEnabled,
                    mediumAlertLullabyId: routineData.lullabyPlayerConfiguration.mediumAlertLullabyId,
                    highAlertLullabyId: routineData.lullabyPlayerConfiguration.highAlertLullabyId,
                    enablePeriodicLullaby: routineData.lullabyPlayerConfiguration.enablePeriodicLullaby,
                    periodicLullabyIntervalMinutes: routineData.lullabyPlayerConfiguration.periodicLullabyIntervalMinutes,
                    periodicLullabyId: routineData.lullabyPlayerConfiguration.periodicLullabyId,
                    enableWakeUpLullaby: routineData.lullabyPlayerConfiguration.enableWakeUpLullaby,
                    wakeUpLullabyId: routineData.lullabyPlayerConfiguration.wakeUpLullabyId
                }
            };

            const endpoint = currentRoutine
                ? `/api/auth/me/sleep/routines/update/${currentRoutine.id}`
                : '/api/auth/me/sleep/routines/create';

            const method = currentRoutine ? 'PUT' : 'POST';

            await apiCall(endpoint, method, dataToSend);
            setShowForm(false);
            fetchData();
        } catch (error) {
            console.error('Failed to save routine:', error);
            alert(error.response?.data?.message || 'Failed to save routine');
        }
    };

    const handleAssignBaby = async (routineId) => {
        try {
            const activeSession = await apiCall('/api/auth/me/sleep/session/current');
            if (activeSession && activeSession.id) {
                alert('Please stop any active sleep sessions before assigning routines');
                return;
            }
            setSelectedRoutineId(routineId);
            setShowBabyModal(true);
        } catch (error) {
            if (error.response?.status !== 404) {
                console.error('Error checking active session:', error);
            }
            setSelectedRoutineId(routineId);
            setShowBabyModal(true);
        }
    };

    const handleBabySelect = async (babyId) => {
        try {
            // Get the baby data to check bio-vulnerable status
            const baby = babies.find(b => b.id === babyId);

            const response = await apiCall(
                `/api/auth/me/babies/${babyId}/assign-sleep-routine/${selectedRoutineId}`,
                'POST'
            );

            setShowBabyModal(false);
            alert('Routine assigned successfully');
            fetchData();
        } catch (error) {
            console.error('Failed to assign routine to baby:', error);

            let errorMessage = 'Failed to assign routine to baby';
            const serverMessage = error.response?.data?.message || error.response?.data;

            if (error.response?.status === 400) {
                // Bio-vulnerable validation errors
                if (typeof serverMessage === 'string') {
                    if (serverMessage.includes('bio-vulnerable')) {
                        errorMessage = serverMessage;
                    } else {
                        errorMessage = serverMessage;
                    }
                }
            } else if (error.response?.status === 409) {
                // Conflict - routine already assigned
                errorMessage = 'This routine is already assigned to the selected baby. ' +
                    'Please choose a different routine or baby.';
            } else if (error.response?.status === 404) {
                errorMessage = 'Baby or routine not found. Please refresh and try again.';
            } else {
                errorMessage = serverMessage || errorMessage;
            }

            alert(errorMessage);
        }
    };

    if (loading) {
        return (
            <div className="loading-container">
                <div className="spinner"></div>
                <p>Loading data...</p>
            </div>
        );
    }

    return (
        <div className="sleep-routines-container">
            <div className="sleep-routines-header">
                <h2>Sleep Routines Management</h2>
                <div className="action-buttons">
                    <button className="btn-primary" onClick={handleAddRoutine}>
                        + Create New Routine
                    </button>
                </div>
            </div>

            <div className="all-routines-section">
                <RoutineList
                    routines={routines}
                    onEdit={handleEditRoutine}
                    onDelete={handleDeleteRoutine}
                    onAssign={handleAssignBaby}
                    showAssignButton={true}
                />
            </div>

            {showForm && (
                <RoutineForm
                    routine={currentRoutine}
                    onSubmit={handleFormSubmit}
                    onCancel={() => setShowForm(false)}
                />
            )}

            {showBabyModal && (
                <BabySelectionModal
                    babies={babies}
                    onSelect={handleBabySelect}
                    onClose={() => setShowBabyModal(false)}
                />
            )}
        </div>
    );
};

export default SleepRoutines;