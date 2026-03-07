import { auth0 } from '@/lib/auth0';

const API_URL = process.env.API_URL!;

export async function getJoinBookClub(id: string) {
  const { token } = await auth0.getAccessToken();
  const res = await fetch(`${API_URL}/book-clubs/${id}/join`, {
    headers: {
      Authorization: `Bearer ${token}`,
      Accept: 'application/json',
    },
  });

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(`Error getting the link to club ${id}: ${res.status} ${errorText}`);
  }
  return (await res.json()) as Promise<{ uri: string }>;
}
