import type { NextRequest } from 'next/server';
import { NextResponse } from 'next/server';
import { auth0 } from '@/lib/auth0';

const API_URL = process.env.API_URL!;

export async function POST(
  req: NextRequest,
  { params }: { params: Promise<{ id: string; bookId: string }> },
) {
  const { id: bookClubId, bookId } = await params;

  try {
    const bodyText = await req.text();
    let parsed: Record<string, boolean> = {};
    try {
      parsed = bodyText ? JSON.parse(bodyText) : {};
    } catch (parseErr: unknown) {
      console.error('Failed to parse JSON body', parseErr);
      return NextResponse.json({ error: 'Invalid JSON body' }, { status: 400 });
    }

    const vote = parsed.hasAccepted;

    if (!bookClubId || !bookId || vote == null) {
      return NextResponse.json({ error: 'Missing bookClubId or bookId or vote' }, { status: 400 });
    }

    const { token } = await auth0.getAccessToken();

    const res = await fetch(`${API_URL}/book-clubs/${bookClubId}/books/${bookId}/votes`, {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
        Accept: 'application/json',
      },
      body: bodyText,
    });

    if (!res.ok) {
      const errorText = await res.text();
      return NextResponse.json({ error: errorText }, { status: res.status });
    }

    const data = await res.json();
    return NextResponse.json(data);
  } catch (err) {
    console.error(`Error voting book:`, err);
    return NextResponse.json({ error: `Failed to register vote` }, { status: 500 });
  }
}
