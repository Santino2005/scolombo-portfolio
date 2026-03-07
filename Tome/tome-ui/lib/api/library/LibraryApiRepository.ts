import type LibraryRepositoryInterface from '@/lib/api/library/LibraryRepositoryInterface';
import type { Author, BookData, LibraryEntry } from '@/lib/types/BookData';
import type BookState from './BookStateEnum';
import type BookStateEnum from './BookStateEnum';
import apiFetch from '@/lib/api/api';

interface updateProps {
  currentPage: number;
  readingStatus: BookStateEnum;
}

interface BadLibraryEntry {
  book: BadBook;
  pages: number;
  currentPage: number;
}

class LibraryApiRepository implements LibraryRepositoryInterface {
  async getBooksWithState(state: BookState): Promise<LibraryEntry[]> {
    const url = `${process.env.API_URL}/libraries?filter=${state}`;
    const data = await apiFetch<BadLibraryEntry[]>(url);
    return data.map(
      (libraryEntity: BadLibraryEntry) =>
        ({
          book: toBookData(libraryEntity.book),
          pages: libraryEntity.pages,
          currentPage: libraryEntity.currentPage,
        }) as LibraryEntry,
    );
  }
  async updateBookReadPages(
    bookId: string,
    currentPage: number,
    state: BookState,
  ): Promise<LibraryEntry> {
    return await apiFetch<LibraryEntry, updateProps>(
      `${process.env.API_URL}/libraries/books/${bookId}`,
      {
        method: 'POST',
        body: { currentPage: currentPage, readingStatus: state } as updateProps,
      },
    );
  }
}
export default LibraryApiRepository;

interface BadBook {
  id: string;
  title: string;
  authors: Author[];
  url: string;
  pages: number;
}

const toBookData = (book: BadBook) => {
  return {
    id: book.id,
    title: book.title,
    authors: book.authors,
    url: book.url,
    pages: book.pages,
  } as BookData;
};
