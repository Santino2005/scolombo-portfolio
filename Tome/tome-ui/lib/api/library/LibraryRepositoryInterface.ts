import type { LibraryEntry } from '@/lib/types/BookData';
import type BookStateEnum from '@/lib/api/library/BookStateEnum';

interface LibraryRepositoryInterface {
  getBooksWithState(state: BookStateEnum): Promise<LibraryEntry[]>;
  updateBookReadPages(
    bookId: string,
    currentPage: number,
    state: BookStateEnum,
  ): Promise<LibraryEntry>;
}
export default LibraryRepositoryInterface;
