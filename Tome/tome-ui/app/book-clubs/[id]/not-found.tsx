import GeneralNotFound from '@/components/notFound/NotFound';

export default function BookNotFound() {
  return (
    <GeneralNotFound
      description="The book club you are looking for does not exist or you not belong to it."
      title="Book Club Not Found"
      actionHref="/"
      actionLabel="Go back to the home page"
    />
  );
}
