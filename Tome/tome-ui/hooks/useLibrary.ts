import { useState, useEffect } from 'react';
import type { LibraryEntry } from '@/lib/types/BookData';
import type BookStateEnum from '@/lib/api/library/BookStateEnum';
import type { BookSearchResult } from '@/lib/types/BookSearchResult';

export function useLibrary() {
  const [libraryData, setLibraryData] = useState<Record<BookStateEnum, LibraryEntry[]>>(
    {} as Record<BookStateEnum, LibraryEntry[]>,
  );
  const [collectedLibraryBooks, setCollectedLibraryBooks] = useState<BookSearchResult[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    async function loadLibrary() {
      setLoading(true);
      setError('');
      try {
        const res = await fetch('/api/library/personal');
        if (!res.ok) {
          const data: { message?: string } = await res.json().catch(() => ({}));
          throw new Error(data?.message || `Backend returned status ${res.status}`);
        }

        const data: Record<BookStateEnum, LibraryEntry[]> = await res.json();
        setLibraryData(data);

        const allBooks: BookSearchResult[] = Object.values(data)
          .flat()
          .map((entry) => ({
            id: entry.book.id,
            title: entry.book.title,
            authors: entry.book.authors.map((a) => a.fullName),
            coverUrl: entry.book.url || '/cover-placeholder.png', // <-- ruta corregida
          }));

        setCollectedLibraryBooks(allBooks);
      } catch (err) {
        setError(err instanceof Error ? err.message : String(err));
        setLibraryData({} as Record<BookStateEnum, LibraryEntry[]>);
        setCollectedLibraryBooks([]);
      } finally {
        setLoading(false);
      }
    }

    loadLibrary();
  }, []);

  return { libraryData, collectedLibraryBooks, loading, error };
}
