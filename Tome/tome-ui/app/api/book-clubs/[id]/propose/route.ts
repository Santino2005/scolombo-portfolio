import type { NextRequest } from 'next/server';
import { NextResponse } from 'next/server';
import { auth0 } from '@/lib/auth0';

const API_URL = process.env.API_URL!;

export async function POST(req: NextRequest, { params }: { params: Promise<{ id: string }> }) {
  const { id: bookClubId } = await params;

  try {
    const { bookId } = await req.json();
    if (!bookClubId || !bookId) {
      return NextResponse.json({ error: 'Missing bookClubId or bookId' }, { status: 400 });
    }

    const { token } = await auth0.getAccessToken();

    const res = await fetch(`${API_URL}/book-clubs/${bookClubId}/books/${bookId}`, {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
        Accept: 'application/json',
      },
    });

    if (!res.ok) {
      const errorText = await res.text();
      return NextResponse.json({ error: errorText }, { status: res.status });
    }

    const data = await res.json();
    return NextResponse.json(data);
  } catch (err) {
    console.error('Error proposing book:', err);
    return NextResponse.json({ error: 'Failed to propose book' }, { status: 500 });
  }
}
