// src/pages/Register.js
import React, {useState} from 'react';
import { apiCall, tokenManager } from '../config/Api';

const Register = ({onAuthSuccess}) => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const validateUsername = (username) => {
        const regex = /^[a-z0-9_]+$/;
        return regex.test(username);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        if (!validateUsername(username)) {
            setError('Username must contain only lowercase letters, numbers, and underscores');
            setLoading(false);
            return;
        }

        try {
            const response = await apiCall('/api/auth/signup', 'POST', {
                username,
                email,
                password
            });

            tokenManager.setToken(response.token);
            localStorage.setItem('user', JSON.stringify(response.userId));
            onAuthSuccess(); // Changed from navigate('/select-baby')
        } catch (err) {
            setError(err.message || 'Registration failed. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-form">
            <h2>Register</h2>
            {error && <div className="error">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Username</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value.toLowerCase())}
                        required
                        minLength={3}
                        maxLength={25}
                        pattern="[a-z0-9_]+"
                        title="Lowercase letters, numbers, and underscores only"
                        placeholder="Enter username (3-25 chars)"
                    />
                </div>
                <div className="form-group">
                    <label>Email</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        placeholder="Enter valid email"
                    />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        minLength={6}
                        placeholder="Enter password (min 6 chars)"
                    />
                </div>
                <button type="submit" disabled={loading}>
                    {loading ? 'Registering...' : 'Register'}
                </button>
            </form>
            <p>
                Already have an account? <a href="/login">Login</a>
            </p>
        </div>
    );
};

export default Register;