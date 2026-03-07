import { NextResponse } from 'next/server';
import { auth0 } from '@/lib/auth0';

const API_URL = process.env.API_URL;

export async function GET() {
  const { token } = await auth0.getAccessToken();
  const res = await fetch(`${API_URL}/book-clubs`, {
    headers: { Authorization: `Bearer ${token}`, Accept: 'application/json' },
  });
  const data = await res.json();
  return NextResponse.json(data, { status: res.status });
}
