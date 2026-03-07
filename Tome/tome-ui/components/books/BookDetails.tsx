'use client';

import type { BookData, Author, Tag } from '@/lib/types/BookData';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Carousel, CarouselContent, CarouselItem } from '@/components/ui/carousel';
import { BookStatus } from '@/components/books/BookStatus';
import { useState } from 'react';
import { BookCover } from '@/components/books/BookCover';

interface props {
  book: BookData;
  initialStatus?: 'New_Status' | 'WANT_TO_READ' | 'READING' | 'READ' | 'DNF' | 'NONE';
}

export function BookDetails({ book, initialStatus }: props) {
  const [loaded, setLoaded] = useState(false);

  const metadata = [
    { label: 'Release', value: book.releaseDate ? book.releaseDate.slice(0, 4) : '-' },
    { label: 'Publisher', value: book.publisher ? book.publisher.name : '-' },
    { label: 'Language', value: book.language ? book.language.name : '-' },
    { label: 'Pages', value: book.pages ?? '-' },
  ];

  return (
    <div className="mx-auto max-w-6xl">
      <section className="grid grid-cols-1 gap-1 pt-0 pb-8 lg:grid-cols-[240px_1fr] lg:gap-8">
        {/* Book Cover */}
        <div className="flex justify-center lg:justify-start">
          <div className="relative h-[360px] w-[240px] flex-shrink-0">
            <BookCover
              url={book.url}
              title={book.title}
              className="rounded-lg object-cover shadow-lg"
              sizes="240px"
              priority
            />
          </div>
        </div>
        {/* Book Information */}
        <div className="flex min-w-0 flex-col space-y-4 lg:h-[360px]">
          {/* Title and Author */}
          <div className="space-y-1">
            <h1 className="text-2xl font-bold tracking-tight lg:text-3xl">{book.title}</h1>
            <p className="text-muted-foreground text-lg">
              {book.authors.map((a: Author) => a.fullName + ' ' + a.surname).join(', ')}
            </p>
          </div>

          {/* Synopsis */}
          <div>
            <ScrollArea className="h-32">
              <p className="text-foreground/90 pr-4 text-base leading-relaxed">{book.synopsis}</p>
            </ScrollArea>
          </div>

          {/* Metadata */}
          <div className="grid grid-cols-2 gap-3 lg:grid-cols-4">
            {metadata.map((item) => (
              <div key={item.label} className="bg-muted/50 rounded-lg p-2 text-center">
                <div className="text-muted-foreground mb-1 text-xs">{item.label}</div>
                <div className="text-sm font-semibold">{item.value}</div>
              </div>
            ))}
          </div>

          {/* Tags Carousel */}
          <div className="relative max-w-full cursor-grab overflow-hidden">
            <Carousel
              opts={{
                align: 'start',
                dragFree: true,
              }}
              className="w-full"
            >
              <CarouselContent className="-ml-2">
                {book.tags &&
                  book.tags.length > 0 &&
                  book.tags.map((tag: Tag) => (
                    <CarouselItem key={tag.id} className="basis-auto pl-2">
                      <span className="bg-primary/10 text-primary inline-block rounded-full px-3 py-1 text-sm font-medium whitespace-nowrap">
                        {tag.name}
                      </span>
                    </CarouselItem>
                  ))}
              </CarouselContent>
            </Carousel>
          </div>
        </div>
      </section>
      <BookStatus book={book} />
    </div>
  );
}
