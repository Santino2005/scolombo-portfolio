import { notFound } from 'next/navigation';
import { BookDetails } from '@/components/books/BookDetails';
import { BooksPerCategory } from '@/components/books/BooksPerCategory';
import type BookRepository from '@/lib/api/book/BookRepository';
import ApiBookRepository from '@/lib/api/book/ApiBookRepository';
import type { BookData, BookSearchData } from '@/lib/types/BookData';

interface PageParams {
  params: Promise<{ id: string }>;
}

const REPO: BookRepository = new ApiBookRepository();

export default async function BookDetailPage({ params }: PageParams) {
  const { id } = await params;

  const book = await REPO.getBookById(id);
  if (!book) notFound();

  const allBooks = await REPO.search(book.title.slice(0, 1), null, 0, 15, 'title', 'asc');
  const relatedBooks: BookSearchData[] = allBooks.content.filter((b) => b.id !== book.id);
  return (
    <div className="container mx-auto px-6 py-8 lg:px-8 lg:py-12">
      <BookDetails book={book} />
      {relatedBooks.length > 0 && (
        <BooksPerCategory
          title="Readers also enjoyed"
          books={
            relatedBooks.map((relatedSearchBook: BookSearchData) => ({
              id: relatedSearchBook.id,
              title: relatedSearchBook.title,
              authors: relatedSearchBook.author,
              url: relatedSearchBook.coverUrl,
            })) as BookData[]
          }
        />
      )}
    </div>
  );
}
