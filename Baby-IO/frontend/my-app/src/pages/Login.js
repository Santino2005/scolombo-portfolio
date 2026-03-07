// src/pages/Login.js
import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import { apiCall, tokenManager } from '../config/Api';

const Login = ({ onAuthSuccess, skipAuthCheck }) => {
    const [credential, setCredential] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (skipAuthCheck) {
            tokenManager.removeToken();
            localStorage.removeItem('user');
            localStorage.removeItem('currentBaby');
        }
    }, [skipAuthCheck]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const response = await apiCall('/api/auth/login', 'POST', {
                credential,
                password
            });

            tokenManager.removeToken();
            localStorage.removeItem('user');
            localStorage.removeItem('currentBaby');

            tokenManager.setToken(response.token);
            localStorage.setItem('user', JSON.stringify(response.userId));

            onAuthSuccess();
            navigate('/select-baby');
        } catch (err) {
            setError(err.message || 'Login failed. Please check your credentials and try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-form">
            <h2>Login</h2>
            {error && <div className="error">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Email or Username</label>
                    <input
                        type="text"
                        value={credential}
                        onChange={(e) => setCredential(e.target.value)}
                        required
                        placeholder="Enter email or username"
                    />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        placeholder="Enter password"
                        minLength={6}
                    />
                </div>
                <button type="submit" disabled={loading}>
                    {loading ? 'Logging in...' : 'Login'}
                </button>
            </form>
            <p>
                Don't have an account? <a href="/register">Register</a>
            </p>
        </div>
    );
};

export default Login;