import { NextResponse } from 'next/server';
import { getJoinBookClub } from '@/lib/api/bookClub/ApiBookClub';
import { joinBookClub } from '@/lib/api/BookClub';

export async function GET(req: Request, { params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
  try {
    const data = await getJoinBookClub(id);
    return NextResponse.json(data);
  } catch (error: unknown) {
    console.error(error);
    const message = error instanceof Error ? error.message : 'Unexpected error occurred';
    return NextResponse.json({ error: 'Error getting the link', message }, { status: 500 });
  }
}

export async function POST(req: Request, { params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
  try {
    const data = await joinBookClub(id);

    if (data === undefined) {
      return new NextResponse(null, { status: 204 });
    }

    return NextResponse.json(data);
  } catch (error: unknown) {
    console.error(error);
    const message = error instanceof Error ? error.message : 'Unexpected error occurred';
    return NextResponse.json({ error: 'Error joining book club', message }, { status: 500 });
  }
}
