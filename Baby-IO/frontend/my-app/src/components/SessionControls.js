// src/components/session/SessionControls.js
import React, { useState } from 'react';

const SessionControls = ({
                             session,
                             routines,
                             onStart,
                             onStop,
                             onPause,
                             onResume,
                             onAttendAlert
                         }) => {
    const [selectedRoutine, setSelectedRoutine] = useState('');

    const handleStart = () => {
        if (!selectedRoutine) return;
        onStart(selectedRoutine);
    };

    return (
        <div className="session-controls">
            {!session ? (
                <div className="start-session">
                    <select
                        value={selectedRoutine}
                        onChange={(e) => setSelectedRoutine(e.target.value)}
                    >
                        <option value="">Select a routine</option>
                        {routines.map((routine) => (
                            <option key={routine.id} value={routine.id}>
                                {routine.name}
                            </option>
                        ))}
                    </select>
                    <button onClick={handleStart} disabled={!selectedRoutine}>
                        Start Session
                    </button>
                </div>
            ) : (
                <div className="active-session-controls">
                    {session.status === 'ACTIVE' && (
                        <>
                            <button onClick={onPause}>Pause</button>
                            <button onClick={onStop}>Stop</button>
                        </>
                    )}
                    {session.status === 'PAUSED' && (
                        <>
                            <button onClick={onResume}>Resume</button>
                            <button onClick={onStop}>Stop</button>
                        </>
                    )}
                    {session.hasAlert && (
                        <button onClick={onAttendAlert}>Attend Alert</button>
                    )}
                </div>
            )}
        </div>
    );
};

export default SessionControls;