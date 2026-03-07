'use client';

import type { BookData, BookSearchData } from '@/lib/types/BookData';
import { useDebounce } from '@/lib/hooks/useDebounce';
import { SearchInput } from '@/components/search/SearchInput';
import { SearchDropdown } from '@/components/search/SearchDropdown';
import { BooksPerCategory } from '@/components/books/BooksPerCategory';
import ProgressCarousel from '@/components/books/progress/carousel/ProgressCarousel';
import { useState, useEffect, useCallback, useRef } from 'react';
import { Skeleton } from '@/components/ui/skeleton';

export function HomePage() {
  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState<BookData[]>([]);
  const [searching, setSearching] = useState(false);
  const [error, setError] = useState('');
  const [showDropdown, setShowDropdown] = useState(false);
  const [recommendedBooks, setRecommendedBooks] = useState<BookData[]>([]);
  const [fantasyBooks, setFantasyBooks] = useState<BookData[]>([]);
  const [isInitialLoading, setIsInitialLoading] = useState(true);
  const [totalElements, setTotalElements] = useState(0);
  const searchContainerRef = useRef<HTMLDivElement>(null);
  const debouncedSearchTerm = useDebounce(searchTerm, 300);

  const performSearch = useCallback(async (query: string) => {
    if (!query.trim()) {
      setSearchResults([]);
      setTotalElements(0);
      setError('');
      setShowDropdown(false);
      return;
    }
    setSearching(true);
    setError('');
    setShowDropdown(true);
    try {
      const res = await fetch(`/api/books?search=${encodeURIComponent(query)}`);
      if (!res.ok) {
        const data = await res.json();
        throw new Error(data?.message || 'There was a problem with the search');
      }
      const pageData = await res.json();
      const data: BookData[] = pageData.content.map((book: BookSearchData) => ({
        id: book.id,
        title: book.title,
        authors: book.author ?? [],
        url: book.coverUrl ?? '../public/cover-placeholder.jpg',
      }));

      setSearchResults(data);
      setTotalElements(pageData.totalElements);
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : 'There was a problem with the search. Please try again later.',
      );
      setSearchResults([]);
      setTotalElements(0);
    } finally {
      setSearching(false);
    }
  }, []);

  const handleBookSelect = (book: BookData) => {
    const authorName = book.authors[0] || 'Unknown Author';
    alert(`Selected: ${book.title} by ${authorName}`);
  };

  const clearSearch = () => {
    setSearchTerm('');
    setSearchResults([]);
    setTotalElements(0);
    setError('');
    setShowDropdown(false);
  };

  const handleInputFocus = () => {
    if (searchTerm && (searchResults.length > 0 || searching)) {
      setShowDropdown(true);
    }
  };

  const closeDropdown = () => {
    setShowDropdown(false);
  };

  useEffect(() => {
    performSearch(debouncedSearchTerm);
  }, [debouncedSearchTerm, performSearch]);

  useEffect(() => {
    async function fetchInitialBooks() {
      try {
        const [recommendedRes, fantasyRes] = await Promise.all([
          fetch(`/api/books?search=r`),
          fetch(`/api/books?search=f`),
        ]);

        if (!recommendedRes.ok || !fantasyRes.ok) {
          throw new Error('Failed to fetch initial books');
        }

        const recommendedData: BookData[] = await recommendedRes.json().then((res) =>
          res.content.map((book: BookSearchData) => ({
            id: book.id,
            title: book.title,
            authors: book.author ?? [],
            url: book.coverUrl ?? '/covers/placeholder.jpg',
          })),
        );
        setRecommendedBooks(recommendedData);

        const fantasyData: BookData[] = await fantasyRes.json().then((res) =>
          res.content.map((book: BookSearchData) => ({
            id: book.id,
            title: book.title,
            authors: book.author ?? [],
            url: book.coverUrl ?? '/covers/placeholder.jpg',
          })),
        );
        setFantasyBooks(fantasyData);
      } catch (error) {
        console.error('Failed to fetch initial books:', error);
      } finally {
        setIsInitialLoading(false);
      }
    }
    fetchInitialBooks();
  }, []);

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (
        searchContainerRef.current &&
        !searchContainerRef.current.contains(event.target as Node)
      ) {
        setShowDropdown(false);
      }
    }
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const CategorySkeleton = () => (
    <section className="mt-16">
      <div className="mx-auto max-w-6xl">
        <Skeleton className="mb-4 h-8 w-1/3 rounded-lg" />
        <div className="flex w-full">
          <div className="-ml-4 flex w-full">
            {[...Array(5)].map((_, i) => (
              <div key={i} className="basis-1/2 pl-4 sm:basis-1/3 md:basis-1/4 lg:basis-1/5">
                <div className="aspect-[2/3] w-full">
                  <Skeleton className="h-full w-full rounded-lg" />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );

  return (
    <div className="bg-background min-h-screen">
      <div className="container mx-auto px-4 py-8">
        <div className="mx-auto mb-8 max-w-2xl">
          <div ref={searchContainerRef} className="relative">
            <div onFocus={handleInputFocus}>
              <SearchInput
                value={searchTerm}
                onChange={setSearchTerm}
                placeholder="Search by title, author or ISBN"
                onClear={clearSearch}
              />
            </div>
            <SearchDropdown
              books={searchResults}
              searchTerm={searchTerm}
              totalElements={totalElements}
              isVisible={showDropdown}
              isLoading={searching}
              error={error}
              onBookSelect={handleBookSelect}
              onClose={closeDropdown}
            />
          </div>
        </div>

        <div>
          <ProgressCarousel />
          {isInitialLoading ? (
            <>
              <CategorySkeleton />
              <CategorySkeleton />
            </>
          ) : (
            <>
              <BooksPerCategory title="Books For You" books={recommendedBooks} />
              <BooksPerCategory title="Fantasy" books={fantasyBooks} />
            </>
          )}
        </div>
      </div>
    </div>
  );
}
