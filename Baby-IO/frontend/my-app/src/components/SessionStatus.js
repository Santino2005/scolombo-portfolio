const SessionStatus = ({ session, currentBaby }) => {
    if (!session) return null;

    return (
        <div className="session-status">
            <h3>Session Status</h3>
            <p>Baby: {currentBaby?.name || 'N/A'}</p>
            <p>Status: {session.status || 'N/A'}</p>
            {session.startTime && (
                <p>Started: {new Date(session.startTime).toLocaleString()}</p>
            )}
            {session.plannedDurationMinutes && (
                <p>Planned Duration: {session.plannedDurationMinutes} minutes</p>
            )}
        </div>
    );
};

export default SessionStatus;