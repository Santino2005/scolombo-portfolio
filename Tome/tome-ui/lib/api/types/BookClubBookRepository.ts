import { auth0 } from '@/lib/auth0';

const API_URL = process.env.API_URL!;

const proposedBooksCache = new Map<
  string,
  { timestamp: number; data: PageResponse<BookClubBookDto> }
>();
const PROPOSED_BOOKS_TTL_MS = 3000; // 3s

export interface UserProfileDTO {
  name: string;
  picture: string;
}

export interface ResponseVoteDto {
  bookClubId: string;
  bookId: string;
  user: UserProfileDTO;
  hasAccepted: boolean | null;
}

export type GroupTypeByVote = 'accepted' | 'rejected' | 'missing';

export type VotesMap = Record<GroupTypeByVote, ResponseVoteDto[]>;

export type BookClubBookStatus =
  | 'PROPOSED'
  | 'VOTING'
  | 'APPROVED'
  | 'REJECTED'
  | 'CURRENT'
  | string;

export type ReadingStatus = 'WANT_TO_READ' | 'READING' | 'READ' | 'DNF' | string;

export type MemberReadingStatusMap = Record<ReadingStatus, UserProfileDTO[]>;

export interface BookClubBookDto {
  bookClubId: string;
  bookId: string;
  coverUrl: string;
  status: BookClubBookStatus;
  vote: boolean | null;
  votes?: VotesMap;
  membersReadingStatus?: MemberReadingStatusMap;
  bookClubName?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export async function proposeBookInClub(
  bookClubId: string,
  bookId: string,
): Promise<BookClubBookDto> {
  const { token } = await auth0.getAccessToken();

  const res = await fetch(`${API_URL}/book-clubs/${bookClubId}/books/${bookId}`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${token}`,
      Accept: 'application/json',
    },
  });

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(`Error proposing book: ${res.status} ${errorText}`);
  }

  return res.json();
}

export async function getProposedBooks(bookClubId: string): Promise<PageResponse<BookClubBookDto>> {
  const now = Date.now();
  const cached = proposedBooksCache.get(bookClubId);
  if (cached && now - cached.timestamp < PROPOSED_BOOKS_TTL_MS) {
    return cached.data;
  }
  const { token } = await auth0.getAccessToken();

  const res = await fetch(`${API_URL}/book-clubs/${bookClubId}/books?status=PROPOSED`, {
    headers: {
      Authorization: `Bearer ${token}`,
      Accept: 'application/json',
    },
  });

  if (!res.ok) {
    const errorText = await res.text();
    console.error('Error fetching proposed books:', errorText);
    throw new Error(`Error fetching proposed books: ${res.status} ${errorText}`);
  }
  const json = await res.json();
  try {
    proposedBooksCache.set(bookClubId, { timestamp: now, data: json });
  } catch (err) {
    console.error('Failed to set proposedBooks cache', err);
  }

  return json;
}

export async function getCurrentBook(bookClubId: string): Promise<BookClubBookDto | null> {
  const { token } = await auth0.getAccessToken();

  const res = await fetch(`${API_URL}/book-clubs/${bookClubId}/books/current`, {
    headers: {
      Authorization: `Bearer ${token}`,
      Accept: 'application/json',
    },
    cache: 'no-store',
  });

  if (res.status === 204) return null;
  if (!res.ok) {
    if (res.status === 404) return null;
    const errorText = await res.text();
    console.error('Error fetching current book:', errorText);
    throw new Error(`Error fetching current book: ${res.status} ${errorText}`);
  }

  const text = await res.text();
  if (!text) return null;

  try {
    return JSON.parse(text);
  } catch (err) {
    console.error('Failed to parse JSON in getCurrentBook:', err);
    return null;
  }
}
