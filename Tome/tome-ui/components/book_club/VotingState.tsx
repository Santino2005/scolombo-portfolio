'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { Button } from '@/components/ui/button';
import { BookOpen, ThumbsDown, ThumbsUp } from 'lucide-react';
import ProposeBookView from './ProposeBookView';
import { ClubHeader } from './ClubHeader';
import AvatarDropdown from '@/components/book_club/DropdownAvatars';
import { BookCover } from '@/components/books/BookCover';
import type { BookClubBookDto } from '@/lib/api/types/BookClubBookRepository';
import { toast } from 'sonner';

interface VotingStateProps {
  bookClubBooks: BookClubBookDto[];
  clubId: string;
  clubName: string;
  clubImage?: string;
}

export default function VotingState({
  bookClubBooks,
  clubId,
  clubName,
  clubImage,
}: VotingStateProps) {
  const [showProposeView, setShowProposeView] = useState(false);
  const router = useRouter();

  const [localVotes, setLocalVotes] = useState<Record<string, boolean | null>>(
    () =>
      Object.fromEntries(bookClubBooks.map((b) => [b.bookId, b.vote])) as Record<
        string,
        boolean | null
      >,
  );
  const [pending, setPending] = useState<Record<string, boolean>>({});

  useEffect(() => {
    const mapped = Object.fromEntries(
      bookClubBooks.map((b) => [b.bookId, b.vote]) as [string, boolean | null][],
    ) as Record<string, boolean | null>;

    setLocalVotes((prev) => ({ ...prev, ...mapped }));
  }, [bookClubBooks]);

  const handleVoteClick = async (bookId: string, voteValue: boolean) => {
    setPending((p) => ({ ...p, [bookId]: true }));
    setLocalVotes((prev) => ({ ...prev, [bookId]: voteValue }));

    try {
      const body = {
        hasAccepted: voteValue,
      };
      const res = await fetch(`/api/book-clubs/${clubId}/books/${bookId}/votes`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });

      if (res.ok) {
        const responseBody = await res.json();
        toast.success(voteValue ? 'Book upvoted' : 'Book downvoted');
        setLocalVotes((prev) => ({ ...prev, [bookId]: voteValue }));
        if (responseBody.bookIsWinner) {
          router.refresh();
          toast.success('A winning book has been selected!');
        }
      } else {
        setLocalVotes((prev) => ({
          ...prev,
          [bookId]: bookClubBooks.find((b) => b.bookId === bookId)?.vote ?? null,
        }));
        const errorText = await res.text();
        if (res.status === 400) {
          router.refresh();
          toast.info(`There is already a winning book or voting has ended.`);
          return;
        }
        toast.error('Failed to register vote. Please try again.');

        console.error('Vote failed', res.status, errorText);
      }
    } catch (err) {
      setLocalVotes((prev) => ({
        ...prev,
        [bookId]: bookClubBooks.find((b) => b.bookId === bookId)?.vote ?? null,
      }));
      toast.error('Failed to register vote. Please try again.');
      console.error('Vote failed', err);
    } finally {
      setPending((p) => ({ ...p, [bookId]: false }));
    }
  };

  if (showProposeView) {
    return (
      <ProposeBookView
        bookClubId={clubId}
        onBack={() => {
          setShowProposeView(false);
          router.refresh();
        }}
      />
    );
  }

  return (
    <div className="flex h-full w-full flex-col">
      <ClubHeader id={clubId} name={clubName} image={clubImage} />

      <div className="flex flex-1 flex-col justify-between px-4 sm:px-6 lg:px-8">
        <div className="mx-auto mt-8 w-full max-w-[1200px]">
          <h2 className="u-text-headline-large mb-6">Currently voting on</h2>

          <div
            className="grid justify-start gap-6"
            style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(225px, 1fr))' }}
          >
            {bookClubBooks.length > 0 ? (
              bookClubBooks.map((book) => {
                const currentVote = localVotes[book.bookId] ?? null;
                const isPending = pending[book.bookId];

                return (
                  <div key={book.bookId} className="flex flex-col items-center gap-3">
                    <Link href={`/books/${book.bookId}`} className="group block">
                      <div className="relative aspect-[2/3] w-[225px] overflow-hidden rounded-md shadow-md">
                        <BookCover
                          url={book.coverUrl || '/cover-placeholder.png'}
                          title={book.bookId}
                          className="object-cover transition-transform duration-300 group-hover:scale-105"
                          sizes="(max-width: 640px) 50vw, 225px"
                        />
                      </div>
                    </Link>

                    <div className="flex w-[225px] gap-2">
                      <Button
                        variant={currentVote === false ? 'neutral' : 'outline'}
                        className="flex h-12 w-[108.5px] items-center justify-center rounded-[var(--ds-spacing-s)] border-2 border-[var(--color-button-neutral)] px-2"
                        onClick={() => handleVoteClick(book.bookId, false)}
                        disabled={isPending}
                        aria-pressed={currentVote === false}
                        aria-label={`Vote to reject ${book.bookId}`}
                      >
                        <div style={{ width: 26, height: 26 }}>
                          <ThumbsDown
                            className={
                              currentVote === false ? 'text-background' : 'text-button-neutral'
                            }
                            style={{ width: '100%', height: '100%' }}
                          />
                        </div>
                      </Button>

                      <Button
                        variant={currentVote === true ? 'neutral' : 'outline'}
                        className="flex h-12 w-[108.5px] items-center justify-center rounded-[var(--ds-spacing-s)] border-2 border-[var(--color-button-neutral)] px-2"
                        onClick={() => handleVoteClick(book.bookId, true)}
                        disabled={isPending}
                        aria-pressed={currentVote === true}
                        aria-label={`Vote to accept ${book.bookId}`}
                      >
                        <div style={{ width: 26, height: 26 }}>
                          <ThumbsUp
                            className={
                              currentVote === true ? 'text-background' : 'text-button-neutral'
                            }
                            style={{ width: '100%', height: '100%' }}
                          />
                        </div>
                      </Button>
                    </div>

                    <AvatarDropdown
                      users={(book.votes?.missing || []).map((u) => ({
                        name: u.user.name,
                        picture: u.user.picture,
                      }))}
                    />
                  </div>
                );
              })
            ) : (
              <p className="mt-8 text-left text-gray-500">No books available for voting.</p>
            )}
          </div>
        </div>

        <div className="mx-auto w-full max-w-[1200px]">
          <Button
            className="bg-primary text-primary-foreground hover:bg-primary/90 mt-8 mb-6 w-full gap-2"
            onClick={() => setShowProposeView(true)}
          >
            <BookOpen size={20} />
            Propose book
          </Button>
        </div>
      </div>
    </div>
  );
}
