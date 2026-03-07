// BabyForm.js
import React, { useState, useEffect } from 'react';
import '../styles/BabyForm.css';

const BabyForm = ({ baby, babies = [], onSubmit, onCancel }) => {
    const [formData, setFormData] = useState({
        name: '',
        ageInMonths: '',
        weightInKilograms: '',
        isBioVulnerable: false,
        gender: '',
        medicalNotes: ''
    });
    const [errors, setErrors] = useState({});

    useEffect(() => {
        if (baby) {
            setFormData({
                name: baby.name || '',
                ageInMonths: baby.ageInMonths?.toString() || '',
                weightInKilograms: baby.weightInKilograms?.toString() || '',
                isBioVulnerable: baby.isBioVulnerable || false,
                gender: baby.gender || '',
                medicalNotes: baby.medicalNotes || ''
            });
        }
    }, [baby]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));

        // Clear the error for this field when user starts typing
        if (errors[name]) {
            setErrors(prev => ({
                ...prev,
                [name]: ''
            }));
        }
    };

    const checkDuplicateName = (name) => {
        if (!name.trim()) return false;

        const trimmedName = name.trim().toLowerCase();
        return babies.some(existingBaby =>
            existingBaby.name.toLowerCase() === trimmedName &&
            existingBaby.id !== baby?.id
        );
    };

    const validateForm = () => {
        const newErrors = {};

        if (!formData.name.trim()) {
            newErrors.name = 'Name is required';
        } else if (checkDuplicateName(formData.name)) {
            newErrors.name = 'A baby with this name already exists';
        }

        if (!formData.ageInMonths || isNaN(formData.ageInMonths)) {
            newErrors.ageInMonths = 'Valid age is required';
        } else if (parseInt(formData.ageInMonths) < 0) {
            newErrors.ageInMonths = 'Age must be positive';
        } else if (parseInt(formData.ageInMonths) > 240) { // 20 years in months
            newErrors.ageInMonths = 'Age seems too high';
        }

        if (!formData.weightInKilograms || isNaN(formData.weightInKilograms)) {
            newErrors.weightInKilograms = 'Valid weight is required';
        } else if (parseFloat(formData.weightInKilograms) <= 0) {
            newErrors.weightInKilograms = 'Weight must be positive';
        } else if (parseFloat(formData.weightInKilograms) > 50) { // Reasonable upper limit
            newErrors.weightInKilograms = 'Weight seems too high';
        }

        if (!formData.gender || !['male', 'female'].includes(formData.gender.toLowerCase())) {
            newErrors.gender = 'Gender must be selected';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!validateForm()) return;

        onSubmit({
            name: formData.name.trim(),
            ageInMonths: parseInt(formData.ageInMonths),
            weightInKilograms: parseFloat(formData.weightInKilograms),
            isBioVulnerable: formData.isBioVulnerable,
            gender: formData.gender.toLowerCase(),
            medicalNotes: formData.medicalNotes.trim() || null
        });
    };

    return (
        <div className="modal-overlay">
            <div className="baby-form-card">
                <div className="modal-header">
                    <h2>{baby ? 'Edit Baby Details' : 'Add New Baby'}</h2>
                    <button className="close-btn" onClick={onCancel}>×</button>
                </div>

                <form onSubmit={handleSubmit} className="spaced-form">
                    <div className="form-row">
                        <div className={`form-group ${errors.name ? 'error' : ''}`}>
                            <label>Baby's Name*</label>
                            <input
                                type="text"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                placeholder="Enter baby's name"
                                className="form-input"
                                maxLength="50"
                            />
                            {errors.name && <span className="error-message">{errors.name}</span>}
                        </div>

                        <div className={`form-group ${errors.ageInMonths ? 'error' : ''}`}>
                            <label>Age (months)*</label>
                            <input
                                type="number"
                                name="ageInMonths"
                                min="0"
                                max="240"
                                value={formData.ageInMonths}
                                onChange={handleChange}
                                placeholder="Enter age"
                                className="form-input"
                            />
                            {errors.ageInMonths && <span className="error-message">{errors.ageInMonths}</span>}
                        </div>
                    </div>

                    <div className="form-row">
                        <div className={`form-group ${errors.weightInKilograms ? 'error' : ''}`}>
                            <label>Weight (kg)*</label>
                            <input
                                type="number"
                                name="weightInKilograms"
                                step="0.1"
                                min="0.1"
                                max="50"
                                value={formData.weightInKilograms}
                                onChange={handleChange}
                                placeholder="Enter weight"
                                className="form-input"
                            />
                            {errors.weightInKilograms && <span className="error-message">{errors.weightInKilograms}</span>}
                        </div>

                        <div className={`form-group ${errors.gender ? 'error' : ''}`}>
                            <label>Gender*</label>
                            <select
                                name="gender"
                                value={formData.gender}
                                onChange={handleChange}
                                className="form-input"
                            >
                                <option value="">Select Gender</option>
                                <option value="male">Male</option>
                                <option value="female">Female</option>
                            </select>
                            {errors.gender && <span className="error-message">{errors.gender}</span>}
                        </div>
                    </div>

                    <div className="form-group checkbox-group spaced-checkbox">
                        <label className="checkbox-label">
                            <input
                                type="checkbox"
                                name="isBioVulnerable"
                                checked={formData.isBioVulnerable}
                                onChange={handleChange}
                            />
                            <span>Biologically Vulnerable</span>
                        </label>
                    </div>

                    <div className="form-group spaced-textarea">
                        <label>Medical Notes</label>
                        <textarea
                            name="medicalNotes"
                            value={formData.medicalNotes}
                            onChange={handleChange}
                            placeholder="Enter any special medical considerations..."
                            rows="4"
                            className="form-textarea"
                            maxLength="500"
                        />
                        <small className="char-count">
                            {formData.medicalNotes.length}/500 characters
                        </small>
                    </div>

                    <div className="form-actions spaced-buttons">
                        <button type="button" className="cancel-btn" onClick={onCancel}>
                            Cancel
                        </button>
                        <button type="submit" className="submit-btn">
                            {baby ? 'Save Changes' : 'Add Baby'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default BabyForm;