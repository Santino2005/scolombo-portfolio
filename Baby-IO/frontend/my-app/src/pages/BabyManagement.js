// pages/BabyManagement.js
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiCall } from '../config/Api';
import BabyList from '../components/BabyList';
import BabyForm from '../components/BabyForm';
import BabyDetailsModal from '../components/BabyDetailsModal';
import RoutineManagementModal from '../components/RoutineManagementModal';
import '../styles/BabyManagement.css';

const BabyManagement = ({ onBabySelect }) => {
    const [babies, setBabies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showForm, setShowForm] = useState(false);
    const [showRoutines, setShowRoutines] = useState(false);
    const [showDetails, setShowDetails] = useState(false);
    const [currentBaby, setCurrentBaby] = useState(null);
    const [selectedBaby, setSelectedBaby] = useState(null);
    const [error, setError] = useState(null);
    const [routines, setRoutines] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        fetchBabies();
        // Load the currently selected baby from localStorage
        const storedBaby = localStorage.getItem('currentBaby');
        if (storedBaby) {
            try {
                setSelectedBaby(JSON.parse(storedBaby));
            } catch (e) {
                console.error('Error parsing stored baby:', e);
            }
        }
    }, []);

    const fetchBabies = async () => {
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
    };

    const fetchBabyDetails = async (babyId) => {
        try {
            const routines = await apiCall(`/api/auth/me/babies/${babyId}/sleep-routines`);
            setRoutines(routines || []);
        } catch (error) {
            console.error('Failed to fetch baby routines:', error);
            setError('Failed to load baby routines.');
        }
    };

    const handleAddBaby = () => {
        setCurrentBaby(null);
        setShowForm(true);
    };

    const handleEditBaby = (baby) => {
        setCurrentBaby(baby);
        setShowForm(true);
    };

    const handleSwitchBaby = async (baby) => {
        try {
            // Call the backend to select the baby
            const selectedBabyData = await apiCall(`/api/auth/me/babies/${baby.id}/select`, 'POST');

            // Update local state
            setSelectedBaby(selectedBabyData);

            // Update localStorage
            localStorage.setItem('currentBaby', JSON.stringify(selectedBabyData));

            // Call the parent callback
            onBabySelect(selectedBabyData);

            setError(null);
        } catch (error) {
            console.error('Failed to switch baby:', error);
            setError('Failed to switch baby. Please try again.');
        }
    };

    const handleViewDetails = async (baby) => {
        setCurrentBaby(baby);
        setShowDetails(true);
    };

    const handleDeleteBaby = async (babyId) => {
        if (window.confirm('Are you sure you want to delete this baby?')) {
            try {
                await apiCall(`/api/auth/me/babies/${babyId}`, 'DELETE');

                // If we're deleting the currently selected baby, clear the selection
                if (selectedBaby && selectedBaby.id === babyId) {
                    setSelectedBaby(null);
                    localStorage.removeItem('currentBaby');
                    onBabySelect(null);
                }

                fetchBabies();
            } catch (error) {
                console.error('Failed to delete baby:', error);
                setError('Failed to delete baby. Please try again.');
            }
        }
    };

    const validateBabyName = (name, excludeId = null) => {
        const trimmedName = name.trim().toLowerCase();
        return babies.some(baby =>
            baby.name.toLowerCase() === trimmedName &&
            baby.id !== excludeId
        );
    };

    const handleManageRoutines = (baby) => {
        setCurrentBaby(baby);
        fetchBabyDetails(baby.id);
        setShowRoutines(true);
    };

    const handleAssignRoutine = (routineId) => {
        if (!currentBaby) return;

        apiCall(`/api/auth/me/babies/${currentBaby.id}/assign-sleep-routine/${routineId}`, 'POST')
            .then(() => {
                fetchBabyDetails(currentBaby.id);
            })
            .catch(error => {
                console.error('Failed to assign routine:', error);
                setError('Failed to assign routine.');
            });
    };

    const handleRemoveRoutine = (routineId) => {
        if (!currentBaby) {
            console.error('No current baby selected');
            return;
        }

        // Convert routineId to string and validate
        const routineIdStr = String(routineId);
        if (!routineIdStr || routineIdStr === 'null' || routineIdStr === 'undefined') {
            console.error('Invalid routine ID provided:', routineId);
            setError('Invalid routine ID');
            return;
        }

        // Use the correct endpoint path parameter name
        apiCall(`/api/auth/me/babies/${currentBaby.id}/remove/sleep-routine/${routineIdStr}`, 'POST')
            .then(() => {
                fetchBabyDetails(currentBaby.id);
                setError(null);
            })
            .catch(error => {
                console.error('Failed to remove routine:', error);
                setError('Failed to remove routine. Please try again.');
            });
    };

    // Update the handleFormSubmit function to properly handle new baby creation
    const handleFormSubmit = async (babyData) => {
        try {
            // Check for duplicate names
            const isDuplicate = validateBabyName(babyData.name, currentBaby?.id);
            if (isDuplicate) {
                setError(`A baby with the name "${babyData.name}" already exists. Please choose a different name.`);
                return;
            }

            let updatedBaby;
            if (currentBaby) {
                updatedBaby = await apiCall(`/api/auth/me/babies/${currentBaby.id}`, 'PUT', babyData);

                // If we updated the currently selected baby, update the selection
                if (selectedBaby && selectedBaby.id === currentBaby.id) {
                    setSelectedBaby(updatedBaby);
                    localStorage.setItem('currentBaby', JSON.stringify(updatedBaby));
                    onBabySelect(updatedBaby);
                }
            } else {
                updatedBaby = await apiCall('/api/auth/me/babies', 'POST', babyData);

                // For new babies, automatically select them if they're the first baby
                if (babies.length === 0) {
                    const selectedBaby = await apiCall(`/api/auth/me/babies/${updatedBaby.id}/select`, 'POST');
                    setSelectedBaby(selectedBaby);
                    localStorage.setItem('currentBaby', JSON.stringify(selectedBaby));
                    onBabySelect(selectedBaby);
                }
            }

            setShowForm(false);
            setError(null);

            // Refresh the baby list
            await fetchBabies();

            // If we edited the current baby, update it in state
            if (currentBaby && updatedBaby && currentBaby.id === updatedBaby.id) {
                setCurrentBaby(updatedBaby);
            }
        } catch (error) {
            console.error('Failed to save baby:', error);
            setError('Failed to save baby. Please try again.');
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
        <div className="baby-management-container">
            <div className="baby-management-header">
                <h2>Baby Management</h2>
                <button className="btn-primary" onClick={handleAddBaby}>
                    + Add New Baby
                </button>
            </div>

            {error && (
                <div className="alert error">
                    {error}
                    <button className="btn-text" onClick={() => setError(null)}>Dismiss</button>
                </div>
            )}

            {showForm && (
                <BabyForm
                    baby={currentBaby}
                    babies={babies} // Pass babies for duplicate name validation
                    onSubmit={handleFormSubmit}
                    onCancel={() => {
                        setShowForm(false);
                        setError(null); // Clear any form-related errors
                    }}
                />
            )}

            {showDetails && currentBaby && (
                <BabyDetailsModal
                    baby={currentBaby}
                    details={currentBaby}
                    onClose={() => setShowDetails(false)}
                />
            )}

            {showRoutines && currentBaby && (
                <RoutineManagementModal
                    baby={currentBaby}
                    routines={routines}
                    onClose={() => setShowRoutines(false)}
                    onAssign={handleAssignRoutine}
                    onRemove={handleRemoveRoutine}
                    onManageAllRoutines={() => {
                        setShowRoutines(false);
                        navigate('/routines');
                    }}
                />
            )}

            <BabyList
                babies={babies}
                onEdit={handleEditBaby}
                onDelete={handleDeleteBaby}
                onViewDetails={handleViewDetails}
                onSwitchBaby={handleSwitchBaby}
                onManageRoutines={handleManageRoutines}
                currentBaby={selectedBaby}
            />
        </div>
    );
};

export default BabyManagement;