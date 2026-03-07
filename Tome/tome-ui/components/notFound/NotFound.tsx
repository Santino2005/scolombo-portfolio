import Link from 'next/link';

type NotFoundProps = {
  description: string;
  title: string;
  actionLabel?: string;
  actionHref?: string;
};

export default function NotFound({
  description,
  title,
  actionHref = '/',
  actionLabel = 'Go back home',
}: NotFoundProps) {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center p-4 text-center">
      <h1 className="text-4xl font-bold">404 - {title}</h1>
      <p className="text-muted-foreground mt-4">{description}</p>
      <Link
        href={actionHref}
        prefetch={false}
        className="text-primary mt-6 font-medium hover:underline"
      >
        {actionLabel}
      </Link>
    </div>
  );
}
