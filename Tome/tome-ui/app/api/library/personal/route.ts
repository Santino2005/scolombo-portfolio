import { auth0 } from '@/lib/auth0';
import { NextResponse } from 'next/server';

const API_URL = process.env.API_URL;

export async function GET(req: Request) {
  try {
    const { token } = await auth0.getAccessToken();

    const { searchParams } = new URL(req.url);
    const search = searchParams.get('search') || '';
    const tags = searchParams.get('tags');
    const page = searchParams.get('page') ?? '0';
    const size = searchParams.get('size') ?? '10';

    let url = `${API_URL}/libraries/personal`;
    if (search || tags) {
      const effectiveSearch = search.trim() || 'a';
      url += `?search=${encodeURIComponent(effectiveSearch)}&page=${page}&size=${size}`;
      if (tags) {
        url += `&tags=${encodeURIComponent(tags)}`;
      }
    }

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      const errorText = await response.text().catch(() => 'No error details available');
      return NextResponse.json(
        { message: `Backend error: ${response.status} ${response.statusText}`, details: errorText },
        { status: response.status },
      );
    }

    const data = await response.json();
    return NextResponse.json(data);
  } catch (error) {
    return NextResponse.json(
      {
        message: 'Error connecting to backend service',
        details: error instanceof Error ? error.message : String(error),
        timestamp: new Date().toISOString(),
      },
      { status: 500 },
    );
  }
}
