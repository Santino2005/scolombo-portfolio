import { Button } from '@/components/ui/button';

type ErrorPageProps = {
  title: string;
  description: string;
  showErrorDetails?: boolean;
  error?: Error & { digest?: string };
};

export default function ErrorPage({
  title,
  description,
  showErrorDetails = false,
  error,
}: ErrorPageProps) {
  return (
    <div className="flex h-[60vh] flex-col items-center justify-center text-center">
      <h2 className="mb-4 text-2xl font-bold">{title}</h2>
      <p className="text-muted-foreground mb-6">{description}</p>

      {showErrorDetails && error && (
        <div className="bg-muted mb-6 rounded-md p-4 text-sm">
          <p className="font-medium">Error details:</p>
          <p className="text-muted-foreground">{error.message}</p>
          {error.digest && (
            <p className="text-muted-foreground mt-1 text-xs">Error ID: {error.digest}</p>
          )}
        </div>
      )}

      <Button
        onClick={() => {
          window.location.reload();
        }}
      >
        Try again
      </Button>
    </div>
  );
}
