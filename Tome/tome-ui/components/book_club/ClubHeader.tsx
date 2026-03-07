'use client';

import Image from 'next/image';
import { Button } from '@/components/ui/button';
import { Share2 } from 'lucide-react';
import { useShareBookClub } from '@/hooks/useShareBookClub';

interface Props {
  id: string;
  name: string;
  image?: string;
}

export function ClubHeader({ id, name, image }: Props) {
  const { shareBookClub, loading } = useShareBookClub();
  const initials = name
    .split(' ')
    .map((word) => word[0])
    .slice(0, 2)
    .join('')
    .toUpperCase();
  const handleShare = () => {
    shareBookClub(id);
  };
  return (
    <div className="border-border mx-auto flex w-full max-w-[1200px] items-center justify-between border-b px-4 py-3 shadow-sm">
      <div className="flex items-center gap-2">
        {image && image.trim() !== '' ? (
          <div className="group relative h-[32px] w-[32px] cursor-pointer overflow-hidden rounded-full shadow-md">
            <Image src={image} alt={name} fill className="rounded-full object-cover" sizes="32px" />
          </div>
        ) : (
          <div className="flex h-8 w-8 items-center justify-center rounded-full bg-[#B8A68E] text-sm font-medium text-neutral-100">
            {initials}
          </div>
        )}
        <span className="text-lg font-medium text-[#2B200D]">{name}</span>
      </div>
      <Button
        variant="ghost"
        size="icon"
        onClick={handleShare}
        disabled={loading}
        title="Share club"
      >
        <Share2 size={18} color="#2B200D" />
      </Button>
    </div>
  );
}
