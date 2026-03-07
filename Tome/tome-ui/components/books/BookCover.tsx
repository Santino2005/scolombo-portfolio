'use client';

import { useState } from 'react';
import Image from 'next/image';
import { Skeleton } from '@/components/ui/skeleton';

interface BookCoverProps {
  url: string;
  title: string;
  width?: number;
  height?: number;
  priority?: boolean;
  className?: string;
  sizes?: string;
}

export function BookCover({
  url,
  title,
  width = 200,
  priority = false,
  className = '',
  sizes,
}: BookCoverProps) {
  const [loaded, setLoaded] = useState(false);
  const [error, setError] = useState(false);

  return (
    <div className="relative h-full w-full">
      {/* Skeleton mientras carga */}
      {!loaded && !error && (
        <Skeleton
          className="h-full w-full animate-pulse rounded-lg"
          style={{ backgroundColor: 'var(--ds-neutral-300)' }}
        />
      )}

      {/* Imagen fallback si falla */}
      {error && (
        <Image
          src="/cover-placeholder.png"
          alt="Fallback cover"
          fill
          className="rounded-lg object-cover"
          sizes={sizes || `${width}px`}
          priority={priority}
        />
      )}

      {/* Imagen real */}
      {!error && (
        <Image
          src={url}
          alt={`Cover of ${title}`}
          fill
          className={`rounded-lg object-cover transition-opacity duration-500 ${
            loaded ? 'opacity-100' : 'opacity-0'
          } ${className}`}
          sizes={sizes || `${width}px`}
          onLoad={() => setLoaded(true)}
          onError={() => setError(true)}
          priority={priority}
        />
      )}
    </div>
  );
}
