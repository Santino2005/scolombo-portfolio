import { NextResponse } from 'next/server';
import { auth0 } from '@/lib/auth0';

const API_URL = process.env.API_URL;

export async function POST(req: Request) {
  try {
    const body = await req.json();

    const { token } = await auth0.getAccessToken();

    const backendUrl = `${API_URL}/book-clubs`;

    const backendRes = await fetch(backendUrl, {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(body),
    });

    let data: unknown;
    try {
      data = await backendRes.json();
    } catch {
      data = { message: 'Cant parse backend response' };
    }

    return NextResponse.json(data, { status: backendRes.status });
  } catch (error) {
    console.error('Error in proxy /api/book-clubs:', error);

    return NextResponse.json(
      {
        message: 'Error creating the Book Club',
        details: error instanceof Error ? error.message : String(error),
      },
      { status: 500 },
    );
  }
}
