import Image from 'next/image';

function OverviewSection() {
  const publicLibraryText = 'Explore our library of over 16 thousand books to find your next read.';
  const yourLibraryTect =
    'Organize your personal library. Your next and old reads. Even the ones you decided to forget.';
  const bookClubs = 'Stop reading alone and start reading together with your friends.';
  return (
    <div
      id="overview"
      className="flex h-[100vh] flex-col items-center justify-center bg-[var(--ds-neutral-200)]"
    >
      <div className="mb-10">
        <h2 className="w-fit text-center font-serif text-6xl font-bold text-[var(--ds-neutral-800)]">
          Organize your
          <br />
          reading experience
        </h2>
      </div>
      <div className="flex h-[30%] w-full flex-row items-center justify-center gap-15">
        <Card title="Public library" text={publicLibraryText} logo="/searchLogo.png" />
        <Card title="Your library" text={yourLibraryTect} logo="/yourLibraryLogo.png" />
        <Card title="Book clubs" text={bookClubs} logo="/BookClubLogo.png" />
      </div>
    </div>
  );
}
type CardProps = {
  title: string;
  text: string;
  logo: string;
};
function Card({ title, text, logo }: CardProps) {
  return (
    <div className="h-[80%] w-[13%] rounded-lg bg-[var(--ds-neutral-100)] text-[var(--ds-neutral-700)]">
      <div className="flex h-[25%] items-start justify-end p-5">
        <Image src={logo} alt={logo.replace('.png', '')} width={45} height={45} />
      </div>
      <div className="px-5 pb-10">
        <h3 className="font-sans text-lg font-bold">{title}</h3>
        <p className="font-sans text-sm">{text}</p>
      </div>
    </div>
  );
}

export default OverviewSection;
