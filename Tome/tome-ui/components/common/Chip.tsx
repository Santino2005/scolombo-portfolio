'use client';

import { X } from 'lucide-react';
import { cn } from '@/lib/utils';

interface ChipProps {
  label: string;
  selected?: boolean;
  onRemove?: () => void;
  onClick?: () => void;
  className?: string;
}

export function Chip({ label, selected = false, onRemove, onClick, className }: ChipProps) {
  return (
    <button
      onClick={onClick}
      className={cn(
        'inline-flex items-center justify-center gap-2 rounded-[32px] px-4 py-2',
        'text-base leading-6 font-medium tracking-[0.15px] whitespace-nowrap',
        'transition-colors focus:ring-2 focus:ring-offset-2 focus:outline-none',
        selected
          ? 'bg-[#532713] text-[#ebe6e0] hover:bg-[#6a3419]'
          : 'border-2 border-[#532713] bg-transparent text-[#532713] hover:bg-[#532713]/5',
        className,
      )}
      type="button"
    >
      {label}
      {selected && onRemove && (
        <X
          size={24}
          className="shrink-0 cursor-pointer"
          onClick={(e) => {
            e.stopPropagation();
            onRemove();
          }}
        />
      )}
    </button>
  );
}
