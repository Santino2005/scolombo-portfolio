'use client';

import { useEffect, useState, useCallback } from 'react';
import { useBookSearch } from '@/hooks/userBookSearch';
import { SearchInput } from '@/components/search/SearchInput';
import type { Book } from '@/components/books/BookGrid';
import { BookGrid } from '@/components/books/BookGrid';
import { useSearchParams, useRouter } from 'next/navigation';
import Image from 'next/image';
import SearchLoadingPage from './loading';
import { useDebounce } from '@/lib/hooks/useDebounce';
import { TagFilter } from '@/components/filters/TagFilter';

export default function SearchPage() {
  const router = useRouter();
  const params = useSearchParams();
  const pageSize = 15;

  const param = params.get('param') ?? '';
  const [input, setInput] = useState(param);
  const [selectedTags, setSelectedTags] = useState<string[]>([]);
  const [lastSearch, setLastSearch] = useState(param);
  const [isSearching, setIsSearching] = useState(false);
  const debouncedInput = useDebounce(input, 500);

  const {
    results: books,
    loading,
    loadingMore,
    hasMore,
    page,
    performSearch,
  } = useBookSearch({ source: 'global' });

  useEffect(() => {
    setInput(param);
  }, [param]);

  useEffect(() => {
    const hasQuery = debouncedInput.trim().length > 0;
    const hasTags = selectedTags.length > 0;

    if (!hasQuery && !hasTags) {
      setIsSearching(false);
      return;
    }

    setIsSearching(true);
    performSearch(debouncedInput, 0, pageSize, selectedTags)
      .then(() => setLastSearch(debouncedInput))
      .finally(() => setIsSearching(false));
  }, [debouncedInput, selectedTags, pageSize, performSearch]);

  const onLoadMore = useCallback(() => {
    if (hasMore && !loadingMore) {
      performSearch(lastSearch, page + 1, pageSize, selectedTags);
    }
  }, [hasMore, loadingMore, lastSearch, page, pageSize, selectedTags, performSearch]);

  return (
    <div className="container mx-auto space-y-12 px-6 py-8 lg:px-8 lg:py-12">
      <div className="relative mx-auto mb-8 max-w-6xl">
        <SearchInput
          value={input}
          onChange={(val: string) => {
            setInput(val);
            router.replace(`/search?param=${encodeURIComponent(val)}`);
          }}
          placeholder="Search books"
          onClear={() => setInput('')}
        />
      </div>

      <TagFilter
        selectedTags={selectedTags}
        onSelectedTagsChange={setSelectedTags}
        className="mx-auto max-w-6xl"
      />

      <div className="items-center justify-center space-y-4">
        {(loading || isSearching) && (
          <div className="mx-auto max-w-6xl">
            <SearchLoadingPage pageSize={pageSize} />
          </div>
        )}

        {!input.trim() && selectedTags.length === 0 && !loading && !isSearching && (
          <h2 className="text-center text-4xl text-[var(--ds-neutral-800)]">
            Start typing and discover
            <br /> your next great read.
          </h2>
        )}

        {(input.trim() || selectedTags.length > 0) &&
          !loading &&
          !isSearching &&
          books.length === 0 && (
            <div className="flex h-full w-full flex-col items-center justify-center">
              <h2 className="text-center text-4xl text-[var(--ds-neutral-800)]">
                We couldn’t track that one down.
                <br />
                Try searching by author or title.
              </h2>
              <div className="relative h-48 w-72">
                <Image
                  src="/BookNotFound.png"
                  alt="Book not found"
                  fill
                  className="object-contain"
                  sizes="(max-width: 640px) 100vw, 300px"
                  priority
                />
              </div>
            </div>
          )}

        {(input.trim() || selectedTags.length > 0) &&
          books.length > 0 &&
          !loading &&
          !isSearching && (
            <div className="mx-auto max-w-6xl">
              <BookGrid
                books={books as Book[]}
                hasMore={hasMore}
                loadingMore={loadingMore}
                searching={loading}
                pageSize={pageSize}
                onLoadMore={onLoadMore}
              />
            </div>
          )}
      </div>
    </div>
  );
}
