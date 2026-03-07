'use client';

import { cn } from '@/lib/utils';
import React from 'react';

export function LoadingSpinner({
  message,
  className,
  height = 8,
  width = 8,
}: {
  message?: string;
  className?: string;
  height?: string | number;
  width?: string | number;
}) {
  const heightClass = typeof height === 'number' ? `h-${height}` : undefined;
  const widthClass = typeof width === 'number' ? `w-${width}` : undefined;

  const style: React.CSSProperties | undefined =
    typeof height === 'string' || typeof width === 'string'
      ? {
          ...(typeof height === 'string' ? { height } : {}),
          ...(typeof width === 'string' ? { width } : {}),
        }
      : undefined;

  return (
    <div className={cn('flex flex-col items-center justify-center py-8', className)}>
      <div
        className={cn(
          heightClass ?? 'h-8',
          widthClass ?? 'w-8',
          'text-sidebar-primary animate-spin rounded-full border-4 border-solid border-current border-r-transparent',
        )}
        style={style}
      ></div>
      {message && <p className="mt-4">{message}</p>}
    </div>
  );
}
