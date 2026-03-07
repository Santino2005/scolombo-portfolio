import { Skeleton } from '@/components/ui/skeleton';

type BookGridSkeletonProps = {
  pageSize: number;
};

export function BookGridSkeleton({ pageSize }: BookGridSkeletonProps) {
  return (
    <>
      <div className="grid grid-cols-2 gap-6 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5">
        {Array.from({ length: pageSize }).map((_, index) => (
          <div key={index} className="group block space-y-2">
            <div className="overflow-hidden rounded-lg">
              <div className="relative aspect-[2/3] w-full">
                <Skeleton className="h-full w-full" />
              </div>
            </div>
          </div>
        ))}
      </div>
    </>
  );
}
