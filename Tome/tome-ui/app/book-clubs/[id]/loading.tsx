import { LoadingSpinner } from '@/components/ui/LoadingSpinner';

export default function BookClubLoading() {
  return (
    <div className="container mx-auto h-full justify-center text-center align-middle">
      <LoadingSpinner
        message={'Loading book club...'}
        className={'text-foreground u-text-headline-small h-full'}
        height={12}
        width={12}
      />
    </div>
  );
}
