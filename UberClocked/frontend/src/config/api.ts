export const API_BASE_URL: string = (
    (import.meta.env.VITE_API_URL as string) || "http://localhost:8080"
).replace(/\/+$/, "");
