'use client';

import {
  BookProgressCarousel,
  BookProgressCarouselArrows,
  BookProgressCarouselContent,
  BookProgressCarouselIndicator,
  BookProgressCarouselIndicators,
  BookProgressCarouselItem,
  BookProgressCarouselNext,
  BookProgressCarouselPrevious,
} from '@/components/books/progress/carousel/BookProgressCarousel';

import BookProgressCard from '@/components/books/progress/card/BookProgressCard';
import { useEffect, useState } from 'react';
import type { LibraryEntry } from '@/lib/types/BookData';
import { chunkArray } from '@/lib/utils';
import { Skeleton } from '@/components/ui/skeleton';

function ProgressBookProgressCarousel() {
  const [libraryEntries, setLibraryEntries] = useState<LibraryEntry[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    async function fetchBooks() {
      try {
        const res = await fetch(`/api/library?readingState=READING`);
        const libraryEntries: LibraryEntry[] = await res.json();
        setLibraryEntries(libraryEntries);
      } catch (error) {
        console.error('Failed to fetch books:', error);
      } finally {
        setIsLoading(false);
      }
    }

    fetchBooks();
  }, []);

  const handleBookStatusChange = (bookId: string) => {
    setLibraryEntries((currentEntries) =>
      currentEntries.filter((entry) => entry.book.id !== bookId),
    );
  };

  const rows = 2;
  const columns = 2;

  const CarouselSkeleton = () => (
    <div className="mx-auto w-full max-w-6xl">
      <div className="grid grid-cols-2 gap-6 p-4">
        {[...Array(4)].map((_, i) => (
          <Skeleton key={i} className="h-48 w-full rounded-lg" />
        ))}
      </div>
      <div className="mt-6 flex items-center justify-center gap-4">
        <Skeleton className="h-8 w-16 rounded" />
        <div className="flex gap-2">
          <Skeleton className="h-2 w-2 rounded-full" />
          <Skeleton className="h-2 w-2 rounded-full" />
          <Skeleton className="h-2 w-2 rounded-full" />
        </div>
        <Skeleton className="h-8 w-16 rounded" />
      </div>
    </div>
  );

  return (
    <>
      {isLoading ? (
        <CarouselSkeleton />
      ) : libraryEntries.length === 0 ? (
        <div
          className="mx-auto flex w-full max-w-6xl items-center justify-center rounded-lg border-10 border-dashed border-[var(--ds-neutral-200)] p-8 text-center"
          style={{ minHeight: '350px' }}
        >
          <div className="flex flex-col gap-2">
            <h2 className="text-3xl text-gray-500">Your shelf is waiting!</h2>
            <h3 className="text-3xl text-gray-500">As soon as you start a book,</h3>
            <h4 className="text-3xl text-gray-500"> {"it'll appear here"}</h4>
          </div>
        </div>
      ) : (
        <div className="mx-auto w-full max-w-6xl">
          <BookProgressCarousel opts={{ loop: true }}>
            <BookProgressCarouselContent>
              {chunkArray(libraryEntries, rows * columns).map((group, idx) => (
                <BookProgressCarouselItem key={idx} rows={rows} columns={columns}>
                  {group.map((libraryEntry) => (
                    <BookProgressCard
                      key={libraryEntry.book.id}
                      libraryEntry={libraryEntry}
                      onStatusChange={handleBookStatusChange}
                    />
                  ))}
                </BookProgressCarouselItem>
              ))}
            </BookProgressCarouselContent>
            <BookProgressCarouselArrows>
              <BookProgressCarouselPrevious position="below" className="btn btn-primary">
                Prev
              </BookProgressCarouselPrevious>
              <BookProgressCarouselIndicators>
                {chunkArray(libraryEntries, rows * columns).map((_, idx) => (
                  <BookProgressCarouselIndicator intent={'neutral'} key={idx} />
                ))}
              </BookProgressCarouselIndicators>
              <BookProgressCarouselNext position="below" className="btn btn-primary">
                Next
              </BookProgressCarouselNext>
            </BookProgressCarouselArrows>
          </BookProgressCarousel>
        </div>
      )}
    </>
  );
}
export default ProgressBookProgressCarousel;
