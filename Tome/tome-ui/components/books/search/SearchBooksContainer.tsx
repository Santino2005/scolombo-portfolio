import Link from 'next/link';
import type { BookData } from '@/lib/types/BookData';

import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/carousel';
import { BookCover } from '@/components/books/BookCover';

interface SearchBooksContainerProps {
  books: BookData[];
  searchParam: string;
}

export function SearchBooksContainer({ books, searchParam }: SearchBooksContainerProps) {
  if (!books || books.length === 0) {
    return null;
  }

  return (
    <section className={'mt-4'}>
      <div className="mx-auto max-w-6xl">
        <div className="group relative w-full">
          <Carousel
            opts={{
              align: 'start',
              loop: false,
            }}
            className="w-full"
          >
            <CarouselContent className="-ml-4">
              {books.map((book, index) => (
                <CarouselItem
                  key={book.id}
                  className="basis-1/2 pl-4 sm:basis-1/3 md:basis-1/4 lg:basis-1/5"
                >
                  <Link href={`/books/${book.id}`} className="group block">
                    <div className="overflow-hidden rounded-lg">
                      <div className="relative aspect-[2/3] w-full">
                        <BookCover
                          url={book.url || '/covers/placeholder.jpg'}
                          title={book.title}
                          className="object-cover transition-transform duration-300 hover:scale-105"
                          sizes="(max-width: 640px) 50vw, (max-width: 768px) 33vw, (max-width: 1024px) 25vw, 20vw"
                          priority={index < 3}
                        />
                      </div>
                    </div>
                  </Link>
                </CarouselItem>
              ))}
              <CarouselItem
                key={'seeMoreCard'}
                className="ml-4 flex basis-1/2 items-center justify-center rounded-md bg-[var(--ds-neutral-300)] p-0 sm:basis-1/3 md:basis-1/4 lg:basis-1/5"
              >
                <div className="flex h-full w-full items-center justify-center">
                  <Link href={`/search?param=${searchParam}`}>
                    <p>See more</p>
                  </Link>
                </div>
              </CarouselItem>
            </CarouselContent>
            <CarouselPrevious className="ml-12 hidden transition-opacity duration-300 group-hover:flex" />
            <CarouselNext className="mr-12 hidden transition-opacity duration-300 group-hover:flex" />
          </Carousel>
        </div>
      </div>
    </section>
  );
}
