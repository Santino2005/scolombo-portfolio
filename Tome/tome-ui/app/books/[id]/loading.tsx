import { Skeleton } from '@/components/ui/skeleton';

export default function BookDetailLoading() {
  return (
    <div className="container mx-auto px-4 py-10 md:px-8">
      <div className="grid grid-cols-1 gap-8 md:grid-cols-[280px_1fr] md:gap-12">
        {/* Skeleton para la portada */}
        <Skeleton className="h-[420px] w-full rounded-lg" />

        {/* Skeleton para la información */}
        <div className="flex flex-col space-y-4">
          <Skeleton className="h-10 w-3/4" />
          <Skeleton className="h-6 w-1/3" />
          <div className="flex gap-4 pt-2">
            <Skeleton className="h-8 w-24" />
            <Skeleton className="h-8 w-24" />
            <Skeleton className="h-8 w-24" />
            <Skeleton className="h-8 w-24" />
          </div>
          <div className="space-y-3 pt-4">
            <Skeleton className="h-4 w-full" />
            <Skeleton className="h-4 w-full" />
            <Skeleton className="h-4 w-5/6" />
          </div>
          <div className="flex flex-wrap gap-2 pt-4">
            <Skeleton className="h-6 w-20 rounded-full" />
            <Skeleton className="h-6 w-24 rounded-full" />
            <Skeleton className="h-6 w-16 rounded-full" />
          </div>
        </div>
      </div>
    </div>
  );
}
