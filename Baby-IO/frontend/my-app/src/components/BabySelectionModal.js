import React, { useState } from 'react';
import '../styles/BabySelectionModal.css';

const BabySelectionModal = ({ babies, onSelect, onClose }) => {
    const [selectedBaby, setSelectedBaby] = useState(null);

    const handleSelect = () => {
        if (selectedBaby) {
            onSelect(selectedBaby);
        }
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h3>Assign to Baby</h3>
                <select
                    onChange={(e) => setSelectedBaby(e.target.value)}
                    className="baby-select"
                >
                    <option value="">Select a baby...</option>
                    {babies.map(baby => (
                        <option key={baby.id} value={baby.id}>
                            {baby.name}
                        </option>
                    ))}
                </select>
                <div className="modal-actions">
                    <button
                        onClick={handleSelect}
                        disabled={!selectedBaby}
                        className="btn-primary"
                    >
                        Assign
                    </button>
                    <button onClick={onClose} className="btn-secondary">
                        Cancel
                    </button>
                </div>
            </div>
        </div>
    );
};

export default BabySelectionModal;