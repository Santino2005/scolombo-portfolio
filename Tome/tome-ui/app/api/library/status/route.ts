import { auth0 } from '@/lib/auth0';
import { NextResponse } from 'next/server';

const API_URL = process.env.API_URL;

export async function POST(request: Request) {
  try {
    const body = await request.json();

    const { bookId, readingStatus, currentPage, startedAt, finishedAt } = body;

    if (!bookId) {
      return NextResponse.json({ message: 'Error: bookId required' }, { status: 400 });
    }

    const { token } = await auth0.getAccessToken();

    const backendUrl = `${API_URL}/libraries/books/${bookId}`;

    const response = await fetch(backendUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        readingStatus: readingStatus,
        currentPage: currentPage,
        startedAt: startedAt,
        finishedAt: finishedAt,
      }),
    });

    if (!response.ok) {
      const errorText = await response.text();
      console.error('Backend Error:', errorText);
      return NextResponse.json(
        { message: `Backend Error: ${response.status}`, details: errorText },
        { status: response.status },
      );
    }

    const data = await response.json();
    return NextResponse.json(data);
  } catch (error) {
    console.error('API Route Error:', error);
    return NextResponse.json(
      {
        message: 'Internal server error in Next.js',
        details: error instanceof Error ? error.message : String(error),
      },
      { status: 500 },
    );
  }
}
