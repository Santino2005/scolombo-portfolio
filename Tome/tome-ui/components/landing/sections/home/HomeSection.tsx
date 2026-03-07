'use client';

import { SearchInput } from '@/components/search/SearchInput';
import Image from 'next/image';
import { useState, useEffect, useRef, useCallback } from 'react';

function HomeSection() {
  return (
    <div id="home" className="flex h-[100vh] flex-col items-center justify-center gap-10">
      <div className="">
        <h1 className={`w-fit text-center font-serif text-6xl font-bold`}>
          Finish Books,
          <br />
          Not Just Start Them.
        </h1>
      </div>
      <div>
        <p className="text-center font-sans text-2xl font-normal">
          Discover, manage and track your reading progress on a<br />
          private and secure platform.
        </p>
      </div>
      <DummySearch />
    </div>
  );
}

function DummySearch() {
  const [animatedValue, setAnimatedValue] = useState('');

  const titles = useRef([
    'Search books...',
    'The House in the Cerulean Sea',
    'The Fellowship of the Ring',
    'Seven Husbands of Evelyn Hugo',
    'A Man Called Ove',
    'The Door-to-Door Bookstore',
  ]);

  const titleIndex = useRef(0);
  const charIndex = useRef(0);
  const isDeleting = useRef(false);
  const timeoutRef = useRef<NodeJS.Timeout | null>(null);

  const animate = useCallback(() => {
    const typingSpeed = 150;
    const deletingSpeed = 75;
    const pauseEnd = 2000;
    const pauseStart = 500;

    const currentTitle = titles.current[titleIndex.current];

    if (isDeleting.current) {
      setAnimatedValue(currentTitle.substring(0, charIndex.current - 1));
      charIndex.current--;

      if (charIndex.current === 0) {
        isDeleting.current = false;
        titleIndex.current = (titleIndex.current + 1) % titles.current.length;
        timeoutRef.current = setTimeout(animate, pauseStart);
      } else {
        timeoutRef.current = setTimeout(animate, deletingSpeed);
      }
    } else {
      setAnimatedValue(currentTitle.substring(0, charIndex.current + 1));
      charIndex.current++;

      if (charIndex.current === currentTitle.length) {
        isDeleting.current = true;
        timeoutRef.current = setTimeout(animate, pauseEnd);
      } else {
        timeoutRef.current = setTimeout(animate, typingSpeed);
      }
    }
  }, []);

  useEffect(() => {
    timeoutRef.current = setTimeout(animate, 1000);

    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, [animate]);

  return (
    <div className="flex flex-col items-center">
      <div className="w-[50%]">
        {/*
          - 'value' está controlado por el estado 'animatedValue'.
          - 'onChange' es una función vacía para bloquear la escritura.
          - 'className="pointer-events-none"' deshabilita clicks y el cursor de texto.
        */}
        <SearchInput value={animatedValue} onChange={() => {}} disabled={true} />
      </div>
      <div className="m-5 flex flex-row gap-5">
        <BookCover coverUrl="/bookCoverOne.png" />
        <BookCover coverUrl="/bookCoverTwo.png" />
        <BookCover coverUrl="/bookCoverThree.png" />
        <BookCover coverUrl="/bookCoverFour.png" />
        <BookCover coverUrl="/bookCoverFive.png" />
      </div>
    </div>
  );
}

function BookCover({ coverUrl }: { coverUrl: string }) {
  return <Image src={coverUrl} width={200} height={50} alt="bookCoverOne" />;
}

export default HomeSection;
