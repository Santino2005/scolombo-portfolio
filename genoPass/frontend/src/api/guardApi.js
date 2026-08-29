import { api } from "./client";

export function loginGuard(username, pin) {
    return api.post("/guard/login", null, {
        params: { username, pin },
    });
}

export function registerGuard(username, pin) {
    return api.post("/guard/register", null, {
        params: { username, pin },
    });
}