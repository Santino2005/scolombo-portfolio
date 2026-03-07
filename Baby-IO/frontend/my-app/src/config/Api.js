const getApiBaseUrl = () => {
    const hostname = window.location.hostname;

    if (hostname === 'localhost' || hostname === '127.0.0.1') {
        return 'http://localhost:8080';
    }
    return 'http://52.202.248.7:8080';
};

const getWsBaseUrl = () => {
    const hostname = window.location.hostname;

    if (hostname === 'localhost' || hostname === '127.0.0.1') {
        return 'ws://localhost:8080';
    }
    return 'ws://52.202.248.7:8080';
};

export const API_BASE_URL = getApiBaseUrl();
export const WS_BASE_URL = getWsBaseUrl();

export const tokenManager = {
    getToken: () => localStorage.getItem('authToken'),
    setToken: (token) => localStorage.setItem('authToken', token),
    removeToken: () => localStorage.removeItem('authToken'),
    isAuthenticated: () => !!localStorage.getItem('authToken')
};

const getDefaultFetchConfig = () => {
    const token = tokenManager.getToken();
    return {
        mode: 'cors',
        headers: {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}` })
        }
    };
};

export const apiCall = async (endpoint, method = 'GET', data = null, customConfig = {}) => {
    const url = `${API_BASE_URL}${endpoint}`;
    const config = {
        ...getDefaultFetchConfig(),
        method,
        ...customConfig
    };

    if (data) {
        config.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(url, config);
        const contentType = response.headers.get('content-type');
        const isJson = contentType && contentType.includes('application/json');

        if (!response.ok) {
            if (response.status === 401) {
                tokenManager.removeToken();
                window.location.href = '/login';
                return;
            }

            let errorMessage;
            try {
                const errorData = isJson ? await response.json() : { message: await response.text() };
                errorMessage = errorData.message || `Request failed: ${response.status} ${response.statusText}`;
            } catch (parseError) {
                errorMessage = `Request failed: ${response.status} ${response.statusText}`;
            }
            throw new Error(errorMessage);
        }

        const payload = isJson ? await response.json() : null;
        return payload ?? { success: true };
    } catch (err) {
        if (err.name === 'TypeError' && err.message.includes('Failed to fetch')) {
            throw new Error('Failed to fetch - Network error or server unreachable');
        }
        throw err;
    }
};

export const authenticatedFetch = async (endpoint, options = {}) => {
    return fetch(`${API_BASE_URL}${endpoint}`, {
        ...getDefaultFetchConfig(),
        ...options
    });
};