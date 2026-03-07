'use client';
import { useCallback, useEffect, useState } from 'react';
import type { BookClubDetails } from '@/lib/types/BookClubDetails';

type UseUserBookClubsReturn = {
  bookClubs: BookClubDetails[];
  loading: boolean;
  error: string | null;
  refresh: () => Promise<void>;
};

export function useUserBookClubs(): UseUserBookClubsReturn {
  const [bookClubs, setBookClubs] = useState<BookClubDetails[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refresh = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch('/api/book-clubs', { cache: 'no-store' });
      if (!res.ok) throw new Error(`Error: ${res.status}`);
      const data: BookClubDetails[] = await res.json();
      setBookClubs(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unknown error');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    refresh();
    const handler = () => void refresh();
    window.addEventListener('bookClubs:refresh', handler);
    return () => window.removeEventListener('bookClubs:refresh', handler);
  }, [refresh]);

  return { bookClubs, loading, error, refresh };
}
