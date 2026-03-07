import { useState, useCallback, useRef } from 'react';
import type { BookSearchResult } from '@/lib/types/BookSearchResult';
import { searchUserLibrary, searchGlobalBooks } from '@/lib/api/search/Search';

type SearchFetcher = (
  query: string,
  page?: number,
  size?: number,
  tags?: string[],
) => Promise<{ results: BookSearchResult[]; hasMore: boolean }>;

export type UseBookSearchOptions = {
  source?: 'personal' | 'global';
  fetcher?: SearchFetcher;
};
export function useBookSearch(options?: UseBookSearchOptions) {
  const [results, setResults] = useState<BookSearchResult[]>([]);
  const [loading, setLoading] = useState(false);
  const [loadingMore, setLoadingMore] = useState(false);
  const [error, setError] = useState('');
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  const optionsRef = useRef<UseBookSearchOptions | undefined>(options);
  optionsRef.current = options;

  const getFetcher = (): SearchFetcher => {
    const opts = optionsRef.current;
    if (opts?.fetcher) return opts.fetcher;
    if (opts?.source === 'global') {
      return searchGlobalBooks as SearchFetcher;
    }
    return searchUserLibrary as SearchFetcher;
  };

  const currentRequestRef = useRef(0);

  const performSearch = useCallback(
    async (query: string, nextPage = 0, size = 10, tags: string[] = []) => {
      if (!query.trim() && tags.length === 0) {
        setResults([]);
        setError('');
        setPage(0);
        setHasMore(true);
        return;
      }

      const fetcher = getFetcher();

      if (nextPage > 0) {
        setLoadingMore(true);
      } else {
        setLoading(true);
      }
      setError('');

      const requestId = ++currentRequestRef.current;

      try {
        const { results: newResults, hasMore: newHasMore } = await fetcher(
          query,
          nextPage,
          size,
          tags,
        );

        if (requestId !== currentRequestRef.current) return;

        setResults((prev) => (nextPage === 0 ? newResults : [...prev, ...newResults]));
        setPage(nextPage);
        setHasMore(newHasMore);
      } catch (err) {
        setError(err instanceof Error ? err.message : String(err));
        if (nextPage === 0) setResults([]);
      } finally {
        if (requestId === currentRequestRef.current) {
          setLoading(false);
          setLoadingMore(false);
        }
      }
    },
    [],
  );
  const clearResults = useCallback(() => {
    setResults([]);
    setError('');
    setPage(0);
    setHasMore(true);
  }, []);

  return { results, loading, loadingMore, error, hasMore, page, performSearch, clearResults };
}
