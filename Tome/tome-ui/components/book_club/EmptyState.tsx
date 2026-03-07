'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { Button } from '@/components/ui/button';
import { BookOpen } from 'lucide-react';
import ProposeBookView from './ProposeBookView';
import { ClubHeader } from './ClubHeader';

interface EmptyStateProps {
  clubId: string;
  clubName: string;
  clubImage?: string;
}

export default function EmptyState({ clubId, clubName, clubImage }: EmptyStateProps) {
  const [showProposeView, setShowProposeView] = useState(false);
  const router = useRouter();

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

      <div className="flex flex-1 flex-col items-center justify-between px-4 sm:px-6 lg:px-8">
        <div className="flex flex-1 items-center justify-center text-center text-[#2B200D]">
          <div className="flex flex-col gap-2 text-[36px] font-normal tracking-normal">
            Got a great read in mind? <br /> Share it with the club!
          </div>
        </div>

        <Button
          className="bg-primary text-primary-foreground hover:bg-primary/90 mb-6 w-full max-w-[1200px] gap-2"
          onClick={() => setShowProposeView(true)}
        >
          <BookOpen size={20} />
          Propose book
        </Button>
      </div>
    </div>
  );
}
