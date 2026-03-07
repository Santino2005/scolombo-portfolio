import { BookGridSkeleton } from '@/components/books/skeletons/BookGridSkeleton';

type SearchLoadingPageProps = {
  pageSize: number;
};

function SearchLoadingPage({ pageSize }: SearchLoadingPageProps) {
  return (
    <>
      <BookGridSkeleton pageSize={pageSize} />
    </>
  );
}

export default SearchLoadingPage;
