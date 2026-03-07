'use client';

import ErrorPage from '@/components/errorPage/ErrorPage';

export default function BookDetailError({ error }: { error: Error & { digest?: string } }) {
  return (
    <ErrorPage
      title="¡Oops! Something went wrong"
      description="Failed to load the page"
      error={error}
      showErrorDetails={process.env.NODE_ENV === 'development'}
    />
  );
}
