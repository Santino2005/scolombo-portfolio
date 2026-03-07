'use client';

import { useState, useEffect, useRef } from 'react';
import { BooksPerCategory } from '@/components/books/BooksPerCategory';
import { SearchInput } from '@/components/search/SearchInput';
import { useDebounce } from '@/lib/hooks/useDebounce';
import Image from 'next/image';
import { LoadingSpinner } from '@/components/ui/LoadingSpinner';
import { BookGrid } from '@/components/books/BookGrid';
import { useBookSearch } from '@/hooks/userBookSearch';
import { useLibrary } from '@/hooks/useLibrary';
import { TagFilter } from '@/components/filters/TagFilter';

export default function LibraryProgressPage() {
  const { libraryData, loading: libraryLoading, error: libraryError } = useLibrary();

  const {
    results: searchResults,
    loading: searching,
    loadingMore,
    error: searchError,
    hasMore,
    page,
    performSearch,
    clearResults,
  } = useBookSearch();

  const [searchTerm, setSearchTerm] = useState('');
  const [selectedTags, setSelectedTags] = useState<string[]>([]);
  const debouncedSearchTerm = useDebounce(searchTerm, 300);
  const searchContainerRef = useRef<HTMLDivElement>(null);
  const [hasSearched, setHasSearched] = useState(false);

  useEffect(() => {
    const hasQuery = debouncedSearchTerm.trim().length > 0;
    const hasTags = selectedTags.length > 0;

    if (hasQuery || hasTags) {
      const searchQuery = debouncedSearchTerm.trim() || 'a';
      performSearch(searchQuery, 0, 10, selectedTags);
      setHasSearched(true);
    } else {
      clearResults();
      setHasSearched(false);
    }
  }, [debouncedSearchTerm, selectedTags, performSearch, clearResults]);

  if (libraryLoading) {
    return (
      <div className="container mx-auto py-12 text-center">
        <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-current border-r-transparent"></div>
        <p className="mt-4 text-gray-600">Loading your library...</p>
      </div>
    );
  }

  if (libraryError) {
    return (
      <div className="container mx-auto p-6">
        <p className="text-red-600">Error: {libraryError}</p>
      </div>
    );
  }

  const isFiltering = searchTerm.trim().length > 0 || selectedTags.length > 0;

  return (
    <div className="container mx-auto space-y-12 px-6 py-8 lg:px-8 lg:py-12">
      <div ref={searchContainerRef} className="relative mx-auto mb-8 max-w-2xl">
        <SearchInput
          value={searchTerm}
          onChange={setSearchTerm}
          placeholder="Search in your personal library..."
          onClear={() => {
            setSearchTerm('');
            clearResults();
          }}
        />
      </div>

      <TagFilter
        selectedTags={selectedTags}
        onSelectedTagsChange={setSelectedTags}
        className="mx-auto max-w-6xl"
      />

      {isFiltering ? (
        <div className="space-y-4">
          {searching && <LoadingSpinner message="Searching your books..." />}

          {!searching && hasSearched && searchResults.length === 0 && (
            <div className="flex flex-col items-center justify-center py-24 text-center">
              <p className="text-2xl font-medium text-[color:var(--ds-neutral-800)]">
                We couldn’t track that one down.
              </p>
              <p className="mt-2 text-lg text-[color:var(--ds-neutral-800)]">
                Try searching by author or title.
              </p>
              <Image
                src="/BookNotFound.png"
                alt="Book not found"
                className="-mt-13"
                width={377}
                height={327}
              />
            </div>
          )}

          {searchResults.length > 0 && (
            <div className="mx-auto max-w-6xl">
              <BookGrid
                books={searchResults}
                hasMore={hasMore}
                loadingMore={loadingMore}
                onLoadMore={() => {
                  const searchQuery = searchTerm.trim() || 'a';
                  performSearch(searchQuery, page + 1, 10, selectedTags);
                }}
              />
            </div>
          )}

          {searchError && <p className="text-center text-red-600">Error: {searchError}</p>}
        </div>
      ) : (
        Object.entries(libraryData).map(([statusKey, entries]) => (
          <BooksPerCategory
            key={statusKey}
            title={statusKey.replaceAll('_', ' ')}
            books={entries.map((entry) => entry.book)}
          />
        ))
      )}
    </div>
  );
}
