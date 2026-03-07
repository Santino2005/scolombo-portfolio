'use client';

import { useState } from 'react';
import { ChevronDown, BookX } from 'lucide-react';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Tooltip, TooltipContent, TooltipTrigger } from '@/components/ui/tooltip';
import type { UserProfileDTO } from '@/lib/api/types/BookClubBookRepository';

interface AvatarDropdownProps {
  users?: UserProfileDTO[];
}

export default function AvatarDropdown({ users = [] }: AvatarDropdownProps) {
  const [open, setOpen] = useState(false);
  const maxVisible = 5;

  const visibleUsers = users.slice(0, maxVisible);
  const remaining = users.length - maxVisible;

  return (
    <div className="relative w-[225px]">
      <button
        onClick={() => setOpen(!open)}
        className="flex w-full items-center justify-between rounded-md px-3 py-2 transition-colors"
        style={{ backgroundColor: '#EBE6E0' }}
      >
        <div className="flex items-center gap-2">
          <BookX size={16} style={{ color: '#2D150A' }} />
          <span className="text-sm font-medium" style={{ color: '#2D150A' }}>
            Missing
          </span>
        </div>

        <ChevronDown
          size={16}
          className={`transition-transform ${open ? 'rotate-180' : ''}`}
          style={{ color: '#2D150A' }}
        />
      </button>

      {open && (
        <div
          className="absolute left-0 z-10 mt-1 w-full rounded-md px-3 py-2"
          style={{ backgroundColor: '#EBE6E0' }}
        >
          <div className="flex justify-start -space-x-3">
            {visibleUsers.map((user) => (
              <Tooltip key={user.picture || user.name}>
                <TooltipTrigger asChild>
                  <Avatar className="h-11 w-11 border-2 border-[var(--ds-neutral-200)]">
                    <AvatarImage src={user.picture} alt={user.name} />
                    <AvatarFallback>{user.name[0]}</AvatarFallback>
                  </Avatar>
                </TooltipTrigger>
                <TooltipContent sideOffset={6}>{user.name}</TooltipContent>
              </Tooltip>
            ))}

            {remaining > 0 && (
              <Avatar className="h-11 w-11 border-2 border-[var(--ds-neutral-200)]">
                <AvatarFallback>+{remaining}</AvatarFallback>
              </Avatar>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
