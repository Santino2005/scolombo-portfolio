import { useState } from 'react';
import { toast } from 'sonner';

export function useShareBookClub() {
  const [loading, setLoading] = useState(false);

  const shareBookClub = async (id: string) => {
    try {
      setLoading(true);
      const res = await fetch(`/api/book-clubs/${id}/join`, { method: 'GET' });
      if (!res.ok) throw new Error('Error getting the invitation link');

      const data = await res.json();
      const fullUrl = `${window.location.origin}${data.uri}`;
      await navigator.clipboard.writeText(fullUrl);

      toast.success('Link copied', { description: fullUrl });
      return fullUrl;
    } catch (error) {
      console.error(error);
      toast.error('Can not generate the link.');
      return null;
    } finally {
      setLoading(false);
    }
  };

  return { shareBookClub, loading };
}
