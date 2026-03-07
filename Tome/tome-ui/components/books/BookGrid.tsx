'use client';

import Link from 'next/link';
import { BookCover } from '@/components/books/BookCover';
import { Button } from '@/components/ui/button';
import { LoadingSpinner } from '@/components/ui/LoadingSpinner';

export interface Book {
  id: string | number;
  title: string;
  coverUrl?: string;
}

interface BookGridProps {
  books: Book[];
  hasMore?: boolean;
  loadingMore?: boolean;
  searching?: boolean;
  pageSize?: number;
  onLoadMore?: () => void;
  onBookSelect?: (book: Book) => void;
}

export function BookGrid({
  books,
  hasMore = false,
  loadingMore = false,
  searching = false,
  pageSize = 10,
  onLoadMore,
  onBookSelect,
}: BookGridProps) {
  const shouldShowSeeMore = hasMore && !searching && books.length % pageSize === 0;

  return (
    <>
      <div className="grid grid-cols-2 gap-6 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5">
        {books.map((book, index) => {
          const bookCard = (
            <div className={`overflow-hidden rounded-lg ${onBookSelect ? 'cursor-pointer' : ''}`}>
              <div className="relative aspect-[2/3] w-full">
                <BookCover
                  url={book.coverUrl || '/cover-placeholder.png'}
                  title={book.title}
                  className="object-cover transition-transform duration-300 hover:scale-105"
                  sizes="(max-width: 640px) 50vw, (max-width: 768px) 33vw, (max-width: 1024px) 25vw, 20vw"
                  priority={index < 3}
                />
              </div>
            </div>
          );

          if (onBookSelect) {
            return (
              <div key={book.id} onClick={() => onBookSelect(book)} className="group block">
                {bookCard}
              </div>
            );
          }

          return (
            <Link key={book.id} href={`/books/${book.id}`} className="group block">
              {bookCard}
            </Link>
          );
        })}
      </div>

      {shouldShowSeeMore && (
        <div className="mt-6 flex justify-center">
          {loadingMore ? (
            <LoadingSpinner className="py-2" />
          ) : (
            <Button variant="neutral_outline_no_border" onClick={onLoadMore}>
              See more
            </Button>
          )}
        </div>
      )}
    </>
  );
}
