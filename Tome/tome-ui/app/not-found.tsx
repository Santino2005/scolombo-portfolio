import NotFound from '@/components/notFound/NotFound';

export default function GeneralNotFound() {
  return (
    <NotFound
      description="The place you are looking does not exist."
      title="Page Not Found"
      actionHref="/"
      actionLabel="Go back home"
    />
  );
}
