'use client';

import ErrorPage from '@/components/errorPage/ErrorPage';

export default function BookDetailError({ error }: { error: Error & { digest?: string } }) {
  return (
    <ErrorPage
      title={'¡Oops! something is wrong'}
      description={'Can not get the details of the book'}
      error={error}
      showErrorDetails={process.env.NODE_ENV === 'development'}
    />
  );
}
