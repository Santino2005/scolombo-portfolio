import axios from "axios";
import { APP_CONFIG } from "../constants/config";

export const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || APP_CONFIG.DEFAULT_API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

// Helper interceptor for cleaner error handling
api.interceptors.response.use(
    (response) => response,
    (error) => {
        const message =
            error.response?.data?.message ||
            error.response?.data ||
            error.message ||
            "Error de conexión con el servidor";
        return Promise.reject(new Error(typeof message === "string" ? message : "Error en la petición"));
    }
);
