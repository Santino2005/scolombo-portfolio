'use client';

import React from 'react';
import { Avatar, AvatarImage, AvatarFallback } from '@/components/ui/avatar';
import { Tooltip, TooltipTrigger, TooltipContent } from '@/components/ui/tooltip';
import type { UserProfileDTO } from '@/lib/api/types/BookClubBookRepository';

interface ReadingMembersProps {
  members: UserProfileDTO[];
  emptyMessage?: string;
  maxVisible?: number;
  className?: string;
}

export default function ReadingMembers({
  members,
  emptyMessage = 'No members',
  maxVisible = 5,
  className = '',
}: ReadingMembersProps) {
  const displayed = members.slice(0, maxVisible);
  const remaining = Math.max(0, members.length - maxVisible);

  return (
    <div className={className}>
      <div className="flex -space-x-3">
        {displayed.length > 0 ? (
          displayed.map((m) => (
            <Tooltip key={m.picture ?? m.name}>
              <TooltipTrigger asChild>
                <Avatar className="h-11 w-11 border-2 border-[var(--ds-neutral-200)]">
                  <AvatarImage src={m.picture} alt={m.name} />
                  <AvatarFallback className="bg-[var(--ds-neutral-400)] text-xs font-medium text-neutral-100">
                    {m.name?.[0]?.toUpperCase() ?? '?'}
                  </AvatarFallback>
                </Avatar>
              </TooltipTrigger>
              <TooltipContent sideOffset={6}>{m.name ?? 'Unknown'}</TooltipContent>
            </Tooltip>
          ))
        ) : (
          <p className="text-sm text-gray-500">{emptyMessage}</p>
        )}

        {remaining > 0 && (
          <Avatar className="h-11 w-11 border-2 border-[var(--ds-neutral-200)] bg-[var(--ds-neutral-400)] text-xs font-semibold text-[var(--ds-neutral-800)]">
            <AvatarFallback>+{remaining}</AvatarFallback>
          </Avatar>
        )}
      </div>
    </div>
  );
}
