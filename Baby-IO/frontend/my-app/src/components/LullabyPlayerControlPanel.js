import React, { useState, useEffect } from 'react';
import { apiCall } from '../config/Api';

const LullabyPlayerControlPanel = () => {
    const [playerStatus, setPlayerStatus] = useState({
        enabled: false,
        connected: false,
        playing: false
    });
    const [lullabies, setLullabies] = useState([]);
    const [selectedLullaby, setSelectedLullaby] = useState(null);
    const [loading, setLoading] = useState(false);

    // Fetch available lullabies
    useEffect(() => {
        const fetchLullabies = async () => {
            try {
                const data = await apiCall('/api/auth/me/lullaby-player/lullabies');
                setLullabies(data);
            } catch (err) {
                console.error('Failed to fetch lullabies:', err);
            }
        };
        fetchLullabies();
    }, []);

    // Fetch player status
    const fetchPlayerStatus = async () => {
        setLoading(true);
        try {
            const status = await apiCall('/api/auth/me/lullaby-player/status');
            setPlayerStatus(status || { enabled: false, connected: false, playing: false });
        } catch (err) {
            console.error('Error fetching player status:', err);
        } finally {
            setLoading(false);
        }
    };

    // Refresh status periodically
    useEffect(() => {
        fetchPlayerStatus();
        const interval = setInterval(fetchPlayerStatus, 5000);
        return () => clearInterval(interval);
    }, []);

    const handlePlayerAction = async (action) => {
        setLoading(true);
        try {
            await apiCall(`/api/auth/me/lullaby-player/${action}`, 'POST');
            await fetchPlayerStatus();
        } catch (err) {
            console.error(`Error ${action} player:`, err);
        } finally {
            setLoading(false);
        }
    };

    const handlePlayLullaby = async () => {
        if (!selectedLullaby) {
            alert('Please select a lullaby first');
            return;
        }

        setLoading(true);
        try {
            const playDto = { name: selectedLullaby.name };
            await apiCall('/api/auth/me/lullaby-player/play', 'POST', playDto);
            await fetchPlayerStatus();
        } catch (err) {
            console.error('Error playing lullaby:', err);
            alert(`Failed to play lullaby: ${err.message}`);
        } finally {
            setLoading(false);
        }
    };

    const handleStopLullaby = async () => {
        setLoading(true);
        try {
            await apiCall('/api/auth/me/lullaby-player/stop', 'POST');
            await fetchPlayerStatus();
        } catch (err) {
            console.error('Error stopping lullaby:', err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="lullaby-player-panel">
            <div className="player-status-card">
                <div className="player-header">
                    <span className="player-icon">🎵</span>
                    <h3>Lullaby Player</h3>
                </div>

                {loading ? (
                    <div className="loading-indicator">Loading...</div>
                ) : (
                    <>
                        <div className="status-info">
                            <div className={`status-indicator ${playerStatus?.connected ? 'connected' : 'disconnected'}`}>
                                {playerStatus?.connected ? '🟢 Connected' : '🔴 Disconnected'}
                            </div>
                            <div className={`status-indicator ${playerStatus?.enabled ? 'enabled' : 'disabled'}`}>
                                {playerStatus?.enabled ? '✅ Enabled' : '❌ Disabled'}
                            </div>
                            <div className={`status-indicator ${playerStatus?.playing ? 'playing' : 'stopped'}`}>
                                {playerStatus?.playing ? '▶️ Playing' : '⏹️ Stopped'}
                            </div>
                        </div>

                        {/* Add Enable/Disable/Restart buttons here */}
                        <div className="player-actions">
                            <button
                                onClick={() => handlePlayerAction('enable')}
                                disabled={loading || playerStatus?.enabled}
                                className="action-btn enable-btn"
                            >
                                Enable
                            </button>
                            <button
                                onClick={() => handlePlayerAction('disable')}
                                disabled={loading || !playerStatus?.enabled}
                                className="action-btn disable-btn"
                            >
                                Disable
                            </button>
                            <button
                                onClick={() => handlePlayerAction('restart')}
                                disabled={loading}
                                className="action-btn restart-btn"
                            >
                                Restart
                            </button>
                        </div>
                    </>
                )}
            </div>

            {/* Lullaby Selection and Controls */}
            <div className="lullaby-controls">
                <h3>Playback Controls</h3>

                <div className="lullaby-selector">
                    <select
                        value={selectedLullaby?.name || ''}
                        onChange={(e) => {
                            const lullaby = lullabies.find(l => l.name === e.target.value);
                            setSelectedLullaby(lullaby);
                        }}
                        disabled={loading || !playerStatus?.enabled}
                    >
                        <option value="">Select a lullaby to play</option>
                        {lullabies.map(lullaby => (
                            <option
                                key={lullaby.name}
                                value={lullaby.name}
                                disabled={!lullaby.available}
                            >
                                🎶 {lullaby.name} ({Math.floor(lullaby.durationSeconds / 60)}min)
                                {!lullaby.available && ' (Unavailable)'}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="playback-buttons">
                    <button
                        onClick={handlePlayLullaby}
                        disabled={loading || !playerStatus?.enabled || !selectedLullaby || playerStatus?.playing}
                        className="action-btn play-btn"
                    >
                        ▶️ Play
                    </button>
                    <button
                        onClick={handleStopLullaby}
                        disabled={loading || !playerStatus?.playing}
                        className="action-btn stop-btn"
                    >
                        ⏹️ Stop
                    </button>
                </div>
            </div>

            {/* Lullaby List */}
            <div className="lullaby-list">
                <h3>Available Lullabies</h3>
                <div className="lullaby-grid">
                    {lullabies.map(lullaby => (
                        <div
                            key={lullaby.name}
                            className={`lullaby-item ${selectedLullaby?.name === lullaby.name ? 'selected' : ''}`}
                            onClick={() => setSelectedLullaby(lullaby)}
                        >
                            <div className="lullaby-info">
                                <span className="lullaby-icon">🎶</span>
                                <div>
                                    <h4>{lullaby.name}</h4>
                                    <p className="lullaby-description">{lullaby.description}</p>
                                    <p className="lullaby-meta">
                                        ⏱️ {Math.floor(lullaby.durationSeconds / 60)}:
                                        {(lullaby.durationSeconds % 60).toString().padStart(2, '0')}
                                        {lullaby.recommendedFor && (
                                            <span> | 👶 {lullaby.recommendedFor}</span>
                                        )}
                                    </p>
                                </div>
                            </div>
                            {!lullaby.available && (
                                <div className="unavailable-overlay">Unavailable</div>
                            )}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default LullabyPlayerControlPanel;