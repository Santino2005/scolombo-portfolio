'use client';

import { useState, useEffect } from 'react';
import { BookGrid } from '@/components/books/BookGrid';
import { SearchInput } from '@/components/search/SearchInput';
import { useDebounce } from '@/lib/hooks/useDebounce';
import { useBookSearch } from '@/hooks/userBookSearch';
import { useLibrary } from '@/hooks/useLibrary';
import VotingState from './VotingState';
import { LoadingSpinner } from '@/components/ui/LoadingSpinner';
import { Button } from '@/components/ui/button';
import { ArrowLeft } from 'lucide-react';
import { toast } from 'sonner';
import type { BookClubBookDto } from '@/lib/api/types/BookClubBookRepository';

interface ProposeBookViewProps {
  bookClubId: string;
  onBack: () => void;
  clubName?: string;
  clubImage?: string;
}

export default function ProposeBookView({
  bookClubId,
  onBack,
  clubName,
  clubImage,
}: ProposeBookViewProps) {
  const { collectedLibraryBooks, loading: libraryLoading, error: libraryError } = useLibrary();
  const {
    results: searchResults,
    loading: searching,
    page,
    performSearch,
    clearResults,
    hasMore,
  } = useBookSearch();

  const [searchTerm, setSearchTerm] = useState('');
  const [votingBook, setVotingBook] = useState<BookClubBookDto | null>(null);
  const debouncedSearchTerm = useDebounce(searchTerm, 300);

  useEffect(() => {
    if (debouncedSearchTerm.trim()) performSearch(debouncedSearchTerm, 0);
    else clearResults();
  }, [debouncedSearchTerm, performSearch, clearResults]);

  if (libraryLoading) return <LoadingSpinner message="Loading your library..." />;
  if (libraryError) return <p className="text-red-600">{libraryError}</p>;

  const booksToShow = searchTerm.trim() ? searchResults : collectedLibraryBooks;

  const handlePropose = async (bookId: string) => {
    try {
      const res = await fetch(`/api/book-clubs/${bookClubId}/propose`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ bookId }),
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText || 'Failed to propose book');
      }

      toast.success('Book proposed successfully!');
      onBack();
    } catch (err) {
      console.error('Error proposing book:', err);
      toast.error('Failed to propose book. Please try again.');
    }
  };

  if (votingBook) {
    return (
      <VotingState
        bookClubBooks={[votingBook]}
        clubId={bookClubId}
        clubName={clubName || ''}
        clubImage={clubImage || ''}
      />
    );
  }

  return (
    <div className="container mx-auto space-y-8 px-6 py-8 lg:px-8 lg:py-12">
      <div>
        <Button onClick={onBack} variant="ghost" size="sm" className="flex items-center gap-2">
          <ArrowLeft className="h-4 w-4" />
          Back
        </Button>
      </div>

      <div className="relative mx-auto mb-8 max-w-6xl">
        <SearchInput
          value={searchTerm}
          onChange={setSearchTerm}
          onClear={() => {
            setSearchTerm('');
            clearResults();
          }}
          placeholder="Search your library..."
        />
      </div>

      {searching && <LoadingSpinner message="Searching..." />}

      {booksToShow.length > 0 ? (
        <div className="mx-auto max-w-6xl">
          <BookGrid
            books={booksToShow}
            hasMore={hasMore}
            loadingMore={false}
            onLoadMore={() => performSearch(searchTerm, page + 1)}
            onBookSelect={(book) => handlePropose(String(book.id))}
          />
        </div>
      ) : (
        !searching && (
          <div className="flex flex-col items-center justify-center py-24 text-center">
            <p className="text-2xl font-medium text-[color:var(--ds-neutral-800)]">
              No books found.
            </p>
            <p className="mt-2 text-lg text-[color:var(--ds-neutral-800)]">
              Try searching by title or author.
            </p>
          </div>
        )
      )}
    </div>
  );
}
