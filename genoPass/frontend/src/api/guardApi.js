import { api } from "./client";

export function loginGuard(username, pin) {
    return api.post("/guard/login", null, {
        params: { username, pin },
    });
}