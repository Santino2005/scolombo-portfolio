import type { NextRequest } from 'next/server';
import { NextResponse } from 'next/server';
import type LibraryRepositoryInterface from '@/lib/api/library/LibraryRepositoryInterface';
import { BookStateNames } from '@/lib/api/library/BookStateEnum';
import LibraryApiRepository from '@/lib/api/library/LibraryApiRepository';

const REPO: LibraryRepositoryInterface = new LibraryApiRepository();

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);

  const readingState = searchParams.get('readingState');

  if (readingState === null) {
    throw new Error('No reading state found while getting books from library.');
  }

  return NextResponse.json(await REPO.getBooksWithState(BookStateNames[readingState]));
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const { bookId, currentPage, status } = body;

    if (!bookId) {
      return NextResponse.json({ error: 'Missing bookId' }, { status: 400 });
    }

    const updatedBook = await REPO.updateBookReadPages(bookId, currentPage, status);

    return NextResponse.json({ message: 'Book state updated', book: updatedBook }, { status: 200 });
  } catch (error) {
    console.error('POST /api/library error:', error);
    return NextResponse.json({ error: 'Failed to update book' }, { status: 500 });
  }
}
