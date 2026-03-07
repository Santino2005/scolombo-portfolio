import { auth0 } from '@/lib/auth0';

async function apiFetch<TResponse, TBody = undefined>(
  url: string,
  { method = 'GET', body, ...customOptions }: Omit<RequestInit, 'body'> & { body?: TBody } = {},
): Promise<TResponse> {
  try {
    const { token } = await auth0.getAccessToken();

    const options: RequestInit = {
      method,
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
        Accept: 'application/json',
        ...(customOptions.headers || {}),
      },
      cache: 'no-store',
      ...customOptions,
    };

    if (method !== 'GET' && body !== undefined) {
      options.body = typeof body === 'string' ? body : JSON.stringify(body);
    }

    const res = await fetch(url, options);

    if (!res.ok) {
      const errText = await res.text();
      throw new Error(`API error: ${errText || res.statusText}`);
    }

    const text = await res.text();

    if (!text) {
      return undefined as unknown as TResponse;
    }

    const contentType = (res.headers.get('content-type') || '').toLowerCase();

    // Si es JSON, parsear; si no, devolver texto crudo tipado
    if (contentType.includes('application/json')) {
      try {
        return JSON.parse(text) as TResponse;
      } catch (err) {
        console.error('apiFetch parse error:', err);
        throw err;
      }
    }

    return text as unknown as TResponse;
  } catch (error) {
    console.error('apiFetch error:', error);
    throw error;
  }
}

export default apiFetch;
