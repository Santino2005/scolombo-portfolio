import type { BookSearchResult, LibraryPageDTO } from '@/lib/types/BookSearchResult';
import type Page from '../types/Page';

export async function searchUserLibrary(
  query: string,
  page = 0,
  size = 10,
  tags: string[] = [],
): Promise<{ results: BookSearchResult[]; hasMore: boolean }> {
  const tagsParam = tags.length > 0 ? `&tags=${tags.join(',')}` : '';
  const res = await fetch(
    `/api/library/personal?search=${encodeURIComponent(query)}&page=${page}&size=${size}${tagsParam}`,
  );

  if (!res.ok) {
    const data: { message?: string } = await res.json().catch(() => ({}));
    throw new Error(data?.message || `Backend returned status ${res.status}`);
  }

  const pageData: LibraryPageDTO = await res.json();

  const results = pageData.content.map((item) => ({
    id: item.book.id,
    title: item.book.title,
    authors:
      item.authors?.map((a) => `${a.name} ${a.surname}`) ??
      item.book.authors?.map((a) => `${a.name} ${a.surname}`) ??
      [],
    coverUrl: item.coverUrl || '/cover-placeholder.jpg',
  }));

  return { results, hasMore: !pageData.last };
}

export async function searchGlobalBooks(
  _query: string,
  _page = 0,
  _size = 10,
  _tags: string[] = [],
): Promise<{ results: BookSearchResult[]; hasMore: boolean; totalElements: number }> {
  const tagsParam = _tags.length > 0 ? `&tags=${_tags.join(',')}` : '';
  const res = await fetch(`/api/books?search=${_query}&page=${_page}&size=${_size}${tagsParam}`);
  if (!res.ok) {
    const data: { message?: string } = await res.json().catch(() => ({}));
    throw new Error(data?.message || `Backend returned status ${res.status}`);
  }
  const data = (await res.json()) as Page<BookSearchResult>;
  return {
    results: data.content,
    hasMore: !data.last,
    totalElements: data.totalElements,
  };
}
