import { getBookClubDetails } from '@/lib/api/BookClub';
import EmptyState from '@/components/book_club/EmptyState';
import VotingState from '@/components/book_club/VotingState';
import ReadingState from '@/components/book_club/ReadingState';
import type { BookClubDetails } from '@/lib/types/BookClubDetails';
import type { BookClubBookDto } from '@/lib/api/types/BookClubBookRepository';
import { getProposedBooks, getCurrentBook } from '@/lib/api/types/BookClubBookRepository';
import type { BookData } from '@/lib/types/BookData';
import { notFound } from 'next/navigation';

interface BookClubPageProps {
  params: Promise<{ id: string }>;
}

export default async function BookClubPage({ params }: BookClubPageProps) {
  const { id } = await params;

  const club: BookClubDetails | null = await getBookClubDetails(id);
  if (!club) {
    notFound();
  }

  const currentBook: BookClubBookDto | null = await getCurrentBook(id);

  const proposedResponse = await getProposedBooks(id);
  const proposedBooks: BookClubBookDto[] = proposedResponse.content ?? [];

  const mappedProposedBooks: BookData[] = proposedBooks.map((book) => ({
    id: book.bookId,
    title: '',
    authors: [],
    url: book.coverUrl,
    pages: 0,
  }));

  if (currentBook) {
    return (
      <ReadingState
        clubId={club.id}
        clubName={club.details}
        clubImage={club.imageBase64}
        bookClubBook={currentBook}
        previousBooks={mappedProposedBooks}
      />
    );
  }

  const image = club.imageBase64 && club.imageBase64.trim() !== '' ? club.imageBase64 : undefined;

  if (proposedBooks.length > 0) {
    return (
      <VotingState
        bookClubBooks={proposedBooks}
        clubId={club.id}
        clubName={club.details}
        clubImage={image}
      />
    );
  }

  return <EmptyState clubId={club.id} clubName={club.details} clubImage={image} />;
}
