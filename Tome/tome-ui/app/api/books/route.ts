import ApiBookRepository from '@/lib/api/book/ApiBookRepository';
import { NextResponse } from 'next/server';

const REPO = new ApiBookRepository();

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const search = searchParams.get('search') || '';
  const tagsParam = searchParams.get('tags');
  const tags = tagsParam
    ? tagsParam
        .split(',')
        .map((t) => t.trim())
        .filter((t) => t !== '')
    : [];
  const page = Number(searchParams.get('page')) || 0;
  const size = Number(searchParams.get('size')) || 15;
  const sort = searchParams.get('sort') ?? 'title';
  const direction = (searchParams.get('direction') as 'asc' | 'desc') ?? 'asc';

  try {
    const result = await REPO.search(search, tags, page, size, sort, direction);
    return NextResponse.json(result);
  } catch (error) {
    return NextResponse.json(
      {
        message: 'Error connecting to backend service',
        details: error instanceof Error ? error.message : String(error),
        timestamp: new Date().toISOString(),
        errorType: error instanceof Error ? error.name : typeof error,
      },
      { status: 500 },
    );
  }
}
