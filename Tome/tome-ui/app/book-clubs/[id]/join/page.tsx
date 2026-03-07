import { JoinBookClubCard } from '@/components/book_club/JoinBookClubCard';
import { getBookClubJoinData } from '@/lib/api/BookClub';

interface JoinBookClubPageProps {
  params: Promise<{
    id: string;
  }>;
}

export default async function JoinBookClubPage({ params }: JoinBookClubPageProps) {
  const { id } = await params;
  const clubData = await getBookClubJoinData(id);

  return (
    <main className="flex min-h-screen items-center justify-center bg-[#e9e2d8] p-8">
      <JoinBookClubCard club={clubData} clubId={id} />
    </main>
  );
}
