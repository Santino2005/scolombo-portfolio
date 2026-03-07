import apiFetch from '@/lib/api/api';
import type { BookClubDetails, JoinBookClubDTO } from '@/lib/types/BookClubDetails';

const API_BASE_URL = process.env.API_URL;

export async function getBookClubDetails(id: string): Promise<BookClubDetails | null> {
  const url = `${API_BASE_URL}/book-clubs/${id}`;

  try {
    return await apiFetch<BookClubDetails>(url);
  } catch (error) {
    console.error('Error fetching book club details:', error);
    return null;
  }
}

export async function getBookClubJoinData(id: string): Promise<JoinBookClubDTO> {
  const url = `${API_BASE_URL}/book-clubs/${id}/join/data`;

  try {
    return await apiFetch<JoinBookClubDTO>(url);
  } catch (error) {
    console.error(`Failed to fetch club data for ${id} to join: `, error);
    throw error;
  }
}

export async function joinBookClub(id: string): Promise<undefined> {
  const url = `${API_BASE_URL}/book-clubs/${id}/join`;

  try {
    return await apiFetch<undefined>(url, {
      method: 'POST',
    });
  } catch (error) {
    console.error(`Error joining book club ${id}:`, error);
    throw error;
  }
}
