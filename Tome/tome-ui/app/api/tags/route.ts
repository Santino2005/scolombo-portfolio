import { NextResponse } from 'next/server';
import { auth0 } from '@/lib/auth0';

const API_URL = process.env.API_URL;

export async function GET(request: Request) {
  try {
    const { token } = await auth0.getAccessToken();
    const { searchParams } = new URL(request.url);
    const search = searchParams.get('search') || '';

    const res = await fetch(`${API_URL}/tags?search=${encodeURIComponent(search)}`, {
      headers: {
        Accept: 'application/json',
        Authorization: `Bearer ${token}`,
      },
      cache: 'no-store',
    });

    if (!res.ok) {
      return NextResponse.json(
        { message: 'Failed to fetch tags from backend' },
        { status: res.status },
      );
    }

    const data = await res.json();
    return NextResponse.json(data, { status: 200 });
  } catch (error) {
    return NextResponse.json(
      {
        message: 'Error connecting to backend service',
        details: error instanceof Error ? error.message : String(error),
      },
      { status: 500 },
    );
  }
}
