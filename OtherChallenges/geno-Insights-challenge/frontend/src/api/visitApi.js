import { api } from "./client";

export function registerEntry(dni, sector) {
    return api.post("/visit", null, {
        params: { dni, sector },
    });
}

export function registerExit(qrToken) {
    return api.put(`/visit/exit/${qrToken}`);
}

export function getTodayVisits() {
    return api.get("/visit/today");
}

export function getVisitHistory() {
    return api.get("/visit/history");
}

export function getActiveCredentialByDni(dni) {
    return api.get(`/visit/credential/active/${dni}`);
}

export function downloadVisitHistoryExcel() {
    return api.get("/visit/history/export", {
        responseType: "blob",
    });
}