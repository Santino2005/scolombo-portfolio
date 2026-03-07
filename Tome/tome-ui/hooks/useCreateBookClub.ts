'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';

export function useCreateBookClub() {
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const handleCreate = async (name: string, imageBase64?: string | null) => {
    const trimmedName = name.trim();
    if (!trimmedName) {
      setError('Empty name is not allowed.');
      return;
    }

    const finalImage = imageBase64 ?? generateLetterAvatar(trimmedName);

    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const res = await fetch('/api/book-clubs/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: trimmedName, imageBase64: finalImage }),
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || 'Error creating Book Club.');
      }

      const data: { id: string } = await res.json();
      setSuccess(true);

      window.dispatchEvent(new Event('bookClubs:refresh'));

      router.push(`/book-clubs/${data.id}`);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'Error creating Book Club.');
    } finally {
      setLoading(false);
    }
  };

  return { handleCreate, loading, error, success };
}

function generateLetterAvatar(name: string): string {
  const firstLetter = name.trim().charAt(0).toUpperCase();
  const canvas = document.createElement('canvas');
  const size = 200;
  canvas.width = size;
  canvas.height = size;

  const ctx = canvas.getContext('2d');
  if (!ctx) return '';

  ctx.fillStyle = `hsl(${Math.floor(Math.random() * 360)}, 60%, 75%)`;
  ctx.fillRect(0, 0, size, size);

  ctx.fillStyle = '#333';
  ctx.font = 'bold 100px sans-serif';
  ctx.textAlign = 'center';
  ctx.textBaseline = 'middle';
  ctx.fillText(firstLetter, size / 2, size / 2);

  return canvas.toDataURL('image/png');
}
