import GeneralNotFound from '@/components/notFound/NotFound';

export default function BookNotFound() {
  return (
    <GeneralNotFound
      description="The book you are looking for does not exist."
      title="Book Not Found"
      actionHref="/"
      actionLabel="Go back to the home page"
    />
  );
}
