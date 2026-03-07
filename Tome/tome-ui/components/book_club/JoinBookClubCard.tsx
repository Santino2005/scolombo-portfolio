'use client';

import Image from 'next/image';
import { Button } from '@/components/ui/button';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import type { JoinBookClubDTO } from '@/lib/types/BookClubDetails';
import { toast } from 'sonner';
import { useRouter } from 'next/navigation';
import { Tooltip, TooltipContent, TooltipTrigger } from '@/components/ui/tooltip';
import { useUser } from '@auth0/nextjs-auth0';
import { useEffect, useState } from 'react';

interface Member {
  id: string | number;
  name: string;
}

interface JoinBookClubCardProps {
  club: JoinBookClubDTO;
  clubId: string;
}

export function JoinBookClubCard({ club, clubId }: JoinBookClubCardProps) {
  const initials = club.bookClubName
    .split(' ')
    .map((word) => word[0])
    .slice(0, 2)
    .join('')
    .toUpperCase();

  const MAX_VISIBLE_AVATARS = 5;
  const displayedMembers = club.members.slice(0, MAX_VISIBLE_AVATARS);
  const remainingCount = club.members.length - MAX_VISIBLE_AVATARS;
  const router = useRouter();
  const { user, isLoading: userIsLoading } = useUser();
  const [isMember, setIsMember] = useState(false);

  useEffect(() => {
    if (!userIsLoading && user) {
      const memberIds = club.members.map((member) => member.id.toString());
      setIsMember(memberIds.includes(user.sub || ''));
    }
  }, [user, userIsLoading, club.members]);

  async function handleJoin() {
    const res = await fetch(`/api/book-clubs/${clubId}/join`, {
      method: 'POST',
      credentials: 'include',
    });

    if (res.ok) {
      toast.success('Successfully joined the book club!');
      window.dispatchEvent(new Event('bookClubs:refresh'));
      router.push(`/book-clubs/${clubId}`);
      return;
    } else {
      toast.error(`Error joining book club`);
    }
  }

  return (
    <div className="flex w-full max-w-2xl flex-col items-center rounded-xl bg-[var(--ds-neutral-200)] p-6 font-sans shadow-sm md:flex-row md:items-start md:gap-6">
      <div className="flex-shrink-0">
        {club.imageBase64 ? (
          <Image
            src={`data:image/png;base64,${club.imageBase64}`}
            alt={club.bookClubName}
            className="rounded-full object-cover"
            width={128}
            height={128}
            unoptimized
          />
        ) : (
          <div className="flex h-32 w-32 items-center justify-center rounded-full bg-[#B8A68E] text-4xl font-semibold text-neutral-100">
            {initials}
          </div>
        )}
      </div>

      <div className="flex flex-1 flex-col gap-3 md:pt-2">
        <h2 className="u-text-display-medium font-bold text-[var(--ds-neutral-800)]">
          {club.bookClubName}
        </h2>

        <div className="flex items-center justify-between">
          <div className="flex -space-x-3">
            {displayedMembers.map((member) => (
              <Tooltip key={member.picture || member.name}>
                <TooltipTrigger asChild>
                  <Avatar
                    className="h-11 w-11 border-2 border-[var(--ds-neutral-200)]"
                    key={member.id}
                  >
                    <AvatarImage src={member.picture} alt={member.name} />
                    <AvatarFallback className="bg-[#B8A68E] text-xs font-medium text-neutral-100">
                      {member.name[0]?.toUpperCase() || '?'}
                    </AvatarFallback>
                  </Avatar>
                </TooltipTrigger>
                <TooltipContent sideOffset={6}>{member.name}</TooltipContent>
              </Tooltip>
            ))}

            {remainingCount > 0 && (
              <Avatar className="h-11 w-11 border-2 border-[var(--ds-neutral-200)] bg-[var(--ds-neutral-400)] text-xs font-semibold text-[var(--ds-neutral-800)]">
                <AvatarFallback>+{remainingCount}</AvatarFallback>
              </Avatar>
            )}
          </div>

          <span className="ml-3 text-sm font-medium text-[var(--ds-neutral-600)]">
            {club.members.length} members
          </span>
        </div>

        <div className="mt-3">
          {userIsLoading ? (
            <Button
              variant="secondary"
              className="u-text-title-medium w-full rounded-xl bg-[var(--ds-neutral-300)] text-[var(--ds-neutral-800)] transition-colors hover:bg-[var(--ds-neutral-400)]"
              disabled
            >
              Loading...
            </Button>
          ) : (
            <Button
              variant="secondary"
              onClick={handleJoin}
              className="u-text-title-medium w-full rounded-xl bg-[var(--ds-neutral-300)] text-[var(--ds-neutral-800)] transition-colors hover:bg-[var(--ds-neutral-400)]"
              disabled={isMember}
            >
              {isMember ? 'You are already a member' : 'Join Book Club'}
            </Button>
          )}
        </div>
      </div>
    </div>
  );
}
