'use client';

import { Button } from '@/components/ui/button';
import { BookOpen } from 'lucide-react';
import { toast } from 'sonner';
import { useCreateBookClub } from '@/hooks/useCreateBookClub';

interface Props {
  name: string;
  imageBase64: string | null;
}

export function CreateBookClubButton({ name, imageBase64 }: Props) {
  const { handleCreate, loading, error, success } = useCreateBookClub();

  const onClick = () => {
    const trimmedName = name.trim();
    if (!trimmedName) {
      toast.error('Book Club name cannot be empty.');
      return;
    }
    handleCreate(name, imageBase64);
  };

  return (
    <div className="flex h-full w-full flex-col items-center justify-end space-y-2 bg-[#e9e2d8] pb-4">
      <Button
        className="text-md w-[90%] max-w-[1200px] gap-2 rounded-md bg-[#4B5C86] px-4 py-6 text-white hover:bg-[#3A4666]"
        onClick={onClick}
        disabled={loading}
      >
        <BookOpen size={18} />
        {loading ? 'Creating...' : 'Create Book Club'}
      </Button>
    </div>
  );
}
