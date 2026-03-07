'use client';

import { useEffect, useRef, useState } from 'react';
import { Button } from '@/components/ui/button';
import { ClubHeader } from '@/components/book_club/ClubHeader';
import type {
  BookClubBookDto,
  MemberReadingStatusMap,
  UserProfileDTO,
} from '@/lib/api/types/BookClubBookRepository';
import { BooksPerCategory } from '@/components/books/BooksPerCategory';
import { BookCheck, BookOpenText, BookX } from 'lucide-react';
import type { BookData } from '@/lib/types/BookData';
import ReadingMembers from '@/components/book_club/ReadingMembers';
import Link from 'next/link';
import { BookCover } from '@/components/books/BookCover';
import { toast } from 'sonner';
import { useRouter } from 'next/navigation';
import { LoadingSpinner } from '@/components/ui/LoadingSpinner';

interface ReadingStateProps {
  bookClubBook: BookClubBookDto;
  clubId: string;
  clubName: string;
  clubImage?: string;
  previousBooks?: BookData[];
}

export default function ReadingState({
  bookClubBook,
  clubId,
  clubName,
  clubImage,
  previousBooks = [],
}: ReadingStateProps) {
  const router = useRouter();
  const [isFinishing, setIsFinishing] = useState(false);
  const overlayRef = useRef<HTMLDivElement | null>(null);
  const contentRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (isFinishing && overlayRef.current) {
      overlayRef.current.focus();
    }
    if (contentRef.current) {
      if (isFinishing) {
        try {
          contentRef.current.setAttribute('inert', '');
        } catch {}
        contentRef.current.classList.add('pointer-events-none');
      } else {
        try {
          contentRef.current.removeAttribute('inert');
        } catch {
          // ignore
        }
        contentRef.current.classList.remove('pointer-events-none');
      }
    }
  }, [isFinishing]);

  const membersRaw = bookClubBook.membersReadingStatus;

  const membersMap: MemberReadingStatusMap = membersRaw ?? {};

  const reading: UserProfileDTO[] = membersMap['READING'] ?? [];
  const read: UserProfileDTO[] = membersMap['READ'] ?? [];
  const dnf: UserProfileDTO[] = membersMap['DNF'] ?? [];

  const MAX_VISIBLE_AVATARS = 5;

  const handleFinishBook = async () => {
    if (isFinishing) return;
    setIsFinishing(true);
    const body = {
      status: 'FINISHED',
    };
    const res = await fetch(`/api/book-clubs/${clubId}/books/${bookClubBook.bookId}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    });

    try {
      if (res.ok) {
        toast.success('Book marked as finished!');
        router.refresh();
      } else {
        const errorText = await res.text().catch(() => 'Failed to finish book');
        toast.error(errorText || 'Failed to finish book');
      }
    } catch (err: unknown) {
      console.error('Error finishing book', err);
      toast.error('Failed to finish book');
    } finally {
      setIsFinishing(false);
    }
  };

  return (
    <div className="flex h-full w-full flex-col">
      <ClubHeader id={clubId} name={clubName} image={clubImage} />

      <div
        className="relative mx-auto mt-8 flex w-full max-w-[1200px] flex-col gap-8 px-4 sm:px-6 lg:px-8"
        aria-busy={isFinishing}
      >
        {isFinishing && (
          <div
            ref={overlayRef}
            tabIndex={-1}
            role="status"
            aria-live="polite"
            onKeyDown={(e) => {
              if (e.key === 'Tab') e.preventDefault();
            }}
            className="bg-muted/60 pointer-events-auto absolute inset-0 z-30 flex items-center justify-center dark:bg-black/40"
          >
            <LoadingSpinner message="Finishing book..." />
          </div>
        )}

        <div ref={contentRef} className={`flex flex-col gap-4`} aria-hidden={isFinishing}>
          <div className="flex items-center gap-4">
            <h2 className="u-text-headline-medium text-[var(--ds-neutral-800)]">
              Currently reading
            </h2>
            <Button
              className="flex h-8 items-center justify-center gap-2 bg-[var(--ds-neutral-500)] px-3 py-1.5 text-sm text-white hover:bg-[var(--ds-neutral-600)] disabled:cursor-wait disabled:opacity-60"
              onClick={handleFinishBook}
              disabled={isFinishing}
            >
              <BookX className="h-4 w-4" />
              <span>{isFinishing ? 'Finishing...' : 'Finish book'}</span>
            </Button>
          </div>

          <div className="flex flex-col gap-8 sm:flex-row">
            <Link
              href={`/books/${bookClubBook.bookId}`}
              className="group block"
              tabIndex={isFinishing ? -1 : 0}
            >
              <div className="relative aspect-[2/3] w-[200px] overflow-hidden rounded-md shadow-md">
                <BookCover
                  url={bookClubBook.coverUrl || '/cover-placeholder.png'}
                  title={bookClubBook.bookId}
                  className="object-cover transition-transform duration-300 group-hover:scale-105"
                  sizes="(max-width: 640px) 50vw, 200px"
                  priority={true}
                />
              </div>
            </Link>

            <div className="flex flex-1 flex-col justify-start gap-4">
              <div className="flex flex-col gap-2">
                <div className="flex items-center gap-2">
                  <BookOpenText className="h-5 w-5 text-[var(--ds-neutral-800)]" />
                  <span className="u-text-title-medium text-[var(--ds-neutral-800)]">Reading</span>
                </div>
                <div className="flex items-center justify-between">
                  <ReadingMembers
                    members={reading}
                    emptyMessage="No one is currently reading."
                    maxVisible={MAX_VISIBLE_AVATARS}
                  />
                </div>
              </div>

              <div className="flex flex-col gap-2">
                <div className="flex items-center gap-2">
                  <BookCheck className="h-5 w-5 text-[var(--ds-neutral-800)]" />
                  <span className="u-text-title-medium text-[var(--ds-neutral-800)]">Read</span>
                </div>
                <div className="flex items-center justify-between">
                  <ReadingMembers
                    members={read}
                    emptyMessage="No one has finished yet."
                    maxVisible={MAX_VISIBLE_AVATARS}
                  />
                </div>
              </div>

              <div className="flex flex-col gap-2">
                <div className="flex items-center gap-2">
                  <BookX className="h-5 w-5 text-[var(--ds-neutral-800)]" />
                  <span className="u-text-title-medium text-[var(--ds-neutral-800)]">DNF</span>
                </div>
                <div className="flex items-center justify-between">
                  <ReadingMembers
                    members={dnf}
                    emptyMessage="No DNF yet."
                    maxVisible={MAX_VISIBLE_AVATARS}
                  />
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="mt-10">
          <h2 className="u-text-headline-medium mb-4 text-[var(--ds-neutral-800)]">
            Previously proposed
          </h2>
          {previousBooks.length > 0 ? (
            <BooksPerCategory books={previousBooks} />
          ) : (
            <p className="text-sm text-gray-500">
              The books you&apos;ve read previously will appear here
            </p>
          )}
        </div>
      </div>
    </div>
  );
}
