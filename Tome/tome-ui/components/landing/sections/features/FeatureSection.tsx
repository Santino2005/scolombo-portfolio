'use client';

import Image from 'next/image';
import { useEffect, useState } from 'react';
import { HomeIcon } from 'lucide-react';

type FeatureItem = {
  key: string;
  label: string;
  logo: string;
  src: string;
  alt: string;
};

function PreviewContainer({ src, alt }: { src: string; alt: string }) {
  const [enter, setEnter] = useState(false);

  useEffect(() => {
    let raf1 = 0;
    let raf2 = 0;
    setEnter(false);
    raf1 = requestAnimationFrame(() => {
      raf2 = requestAnimationFrame(() => setEnter(true));
    });
    return () => {
      if (raf1) cancelAnimationFrame(raf1);
      if (raf2) cancelAnimationFrame(raf2);
    };
  }, [src]);

  return (
    <div
      id="feature-image"
      role="img"
      aria-label={alt}
      className="border-button-neutral relative overflow-hidden rounded-lg border-2"
      style={{
        aspectRatio: '1920/908',
        width: '100%',
        transition:
          'opacity 1200ms cubic-bezier(.22,.61,.36,1), transform 1200ms cubic-bezier(.22,.61,.36,1)',
        willChange: 'opacity, transform',
        transform: enter ? 'translateX(0)' : 'translateX(100vw)',
        opacity: enter ? 1 : 0,
        pointerEvents: enter ? 'auto' : 'none',
      }}
      aria-hidden={!enter}
    >
      <Image
        src={src}
        alt={alt}
        fill
        sizes="(min-width: 1024px) 60vw, 100vw"
        className="object-cover"
        priority
      />
    </div>
  );
}

function FeatureSection() {
  const items: FeatureItem[] = [
    {
      key: 'home',
      label: 'Home',
      src: '/home-page-screenshot.png',
      alt: 'Home screenshot',
      logo: '',
    },
    {
      key: 'public',
      label: 'Public Library',
      src: '/public-library-screenshot.png',
      alt: 'Public library screenshot',
      logo: '/searchLogo.png',
    },
    {
      key: 'personal',
      label: 'Your Library',
      src: '/personal-library-screenshot.png',
      alt: 'Personal library screenshot',
      logo: '/yourLibraryLogo.png',
    },
    {
      key: 'clubs',
      label: 'Book Clubs',
      src: '/book-clubs-screenshots.png',
      alt: 'Book clubs screenshot',
      logo: '/BookClubLogo.png',
    },
  ];

  const [selected, setSelected] = useState<string>(items[0].key);
  const selectedItem = items.find((i) => i.key === selected) ?? items[0];

  return (
    <section
      id="features"
      aria-labelledby="features-heading"
      className="flex h-[100vh] items-center justify-center overflow-hidden bg-[var(--ds-neutral-50)]"
    >
      <div className="grid w-[86%] grid-cols-[260px_1fr] gap-10">
        {/* Left navigation */}
        <nav aria-label="Feature sections" className="relative flex flex-col">
          <h2 id="features-heading" className="sr-only">
            Features
          </h2>
          <div className="flex flex-col gap-4">
            {items.map((item) => {
              const isSelected = item.key === selected;

              return (
                <button
                  key={item.key}
                  type="button"
                  onClick={() => setSelected(item.key)}
                  aria-pressed={isSelected}
                  aria-controls="feature-image"
                  className={
                    `w-full cursor-pointer px-5 py-3 text-left font-serif text-2xl transition-all duration-300 ` +
                    (isSelected
                      ? `border-button-neutral rounded-lg border-2 bg-transparent` +
                        ` shadow-[0_6px_20px_rgba(0,0,0,0.03)]`
                      : `border-border rounded-lg border bg-transparent`)
                  }
                  style={{ transition: 'box-shadow 250ms ease, border-color 250ms ease' }}
                >
                  {item.logo ? (
                    <Image
                      src={item.logo}
                      alt={item.label + ' logo'}
                      width={24}
                      height={24}
                      className="mr-3 mb-1 inline-block"
                    />
                  ) : (
                    <HomeIcon
                      width={24}
                      height={24}
                      className="text-muted-foreground mr-3 mb-1 inline-block"
                    />
                  )}
                  {item.label}
                </button>
              );
            })}
          </div>
        </nav>

        {/* Right preview area */}
        <div className="relative flex items-center justify-center">
          {/* Keyed container to force proper mount/enter animation */}
          <PreviewContainer key={selected} src={selectedItem.src} alt={selectedItem.alt} />
        </div>
      </div>
    </section>
  );
}

export default FeatureSection;
