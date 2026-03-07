import type { BookData, BookSearchData } from '@/lib/types/BookData';
import type Page from '../types/Page';

interface BookRepository {
  search(
    searchData: string,
    tags: string[] | null,
    page: number,
    size: number,
    sort: string,
    order: 'asc' | 'desc',
  ): Promise<Page<BookSearchData>>;
  getBookById(id: string): Promise<BookData>;
}

export default BookRepository;
