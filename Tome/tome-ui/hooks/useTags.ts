import { useState, useEffect } from 'react';

interface UseTagsReturn {
  tags: string[];
  loading: boolean;
  error: string;
}

interface TagsResponse {
  names: string[];
}

export function useTags(searchQuery = ''): UseTagsReturn {
  const [tags, setTags] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    async function fetchTags() {
      try {
        const res = await fetch(`/api/tags?search=${encodeURIComponent(searchQuery)}`);

        if (!res.ok) {
          const data: { message?: string } = await res.json().catch(() => ({}));
          throw new Error(data?.message || `Failed to fetch tags: ${res.status}`);
        }

        const data: TagsResponse = await res.json();
        setTags(data.names || []);
      } catch (err) {
        setError(err instanceof Error ? err.message : String(err));
        setTags([]);
      } finally {
        setLoading(false);
      }
    }

    fetchTags();
  }, [searchQuery]);

  return { tags, loading, error };
}
