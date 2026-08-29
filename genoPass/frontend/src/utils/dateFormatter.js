export function formatDate(dateString) {
    if (!dateString) return "-";
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return "-";
    return date.toLocaleDateString("es-AR", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
    });
}

export function formatTime(dateString) {
    if (!dateString) return "-";
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return "-";
    return date.toLocaleTimeString("es-AR", {
        hour: "2-digit",
        minute: "2-digit",
    });
}

export function formatDateTime(dateString) {
    if (!dateString) return "-";
    return `${formatDate(dateString)} ${formatTime(dateString)}`;
}
