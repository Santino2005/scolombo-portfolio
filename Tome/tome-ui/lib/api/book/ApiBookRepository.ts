import type { BookData, BookSearchData } from '@/lib/types/BookData';
import type BookRepository from './BookRepository';
import apiFetch from '../api';
import type Page from '../types/Page';

const baseUrl: string = `${process.env.API_URL}/books`;

class ApiBookRepository implements BookRepository {
  async getBookById(id: string): Promise<BookData> {
    const endpoint: string = `${baseUrl}/${id}`;
    const data = await apiFetch<BookData>(endpoint);
    return data;
  }
  async search(
    searchData: string,
    tags: string[] | null,
    page: number,
    size: number,
    sort: string,
    order: 'asc' | 'desc',
  ): Promise<Page<BookSearchData>> {
    const endpoint = `${baseUrl}?search=${searchData}${
      tags && tags.length >= 1 ? '&tags=' + tags.join(',') : ''
    }&page=${page}&size=${size}&sort=${sort}&direction=${order}`;
    const data = await apiFetch<Page<BookSearchData>>(endpoint);
    return data;
  }
}
export default ApiBookRepository;
