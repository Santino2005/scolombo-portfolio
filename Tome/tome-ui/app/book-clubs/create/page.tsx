'use client';

import Image from 'next/image';
import { useEffect, useRef, useState } from 'react';
import { CreateInput } from '@/components/search/CreateInput';
import { CreateBookClubButton } from '@/components/book_club/CreateBookClubButton';
import { toast } from 'sonner';

export default function CreateBookClubPage() {
  const [name, setName] = useState('');
  const [imagePreview, setImagePreview] = useState<string | null>(null);
  const [imageBase64, setImageBase64] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const MAX_FILE_SIZE = 400 * 1024;
  const MAX_NAME_LENGTH = 30;

  useEffect(() => {
    fetch('/DefaultBookClubLogo.png')
      .then((res) => res.blob())
      .then((blob) => {
        const reader = new FileReader();
        reader.onloadend = () => {
          const base64String = (reader.result as string).split(',')[1];
          setImageBase64(base64String);
        };
        reader.readAsDataURL(blob);
      })
      .catch(() => {
        console.error('Error loading the default image');
      });
  }, []);

  const handleNameChange = (value: string) => {
    if (value.length <= MAX_NAME_LENGTH) {
      setName(value);
    } else {
      toast.error(`Name must be shorter than ${MAX_NAME_LENGTH} characters`);
    }
  };

  const handleImageClick = () => {
    fileInputRef.current?.click();
  };

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    if (!file.type.startsWith('image/')) {
      toast.error('Please select an image file');
      setImagePreview(null);
      e.target.value = '';
      return;
    }
    if (file.size > MAX_FILE_SIZE) {
      toast.error('Image size must be less than 400KB');
      setImagePreview(null);
      return;
    }
    const imageUrl = URL.createObjectURL(file);
    setImagePreview(imageUrl);
    const reader = new FileReader();
    reader.onloadend = () => {
      const base64String = (reader.result as string).split(',')[1];
      setImageBase64(base64String);
    };
    reader.readAsDataURL(file);
  };

  return (
    <div className="flex min-h-screen flex-col bg-[#e9e2d8]">
      <header className="flex flex-col items-center space-y-6 pt-8">
        <div
          className="group relative h-[225px] w-[225px] cursor-pointer overflow-hidden rounded-full shadow-md"
          onClick={handleImageClick}
        >
          <Image
            src={imagePreview || '/DefaultBookClubLogo.png'}
            alt="Club cover"
            fill
            className="rounded-full object-cover transition group-hover:opacity-80"
            sizes="225px"
            priority
          />
          <div className="absolute inset-0 flex items-center justify-center bg-black/40 font-semibold text-white opacity-0 transition group-hover:opacity-100">
            Change Image
          </div>
        </div>

        <input
          ref={fileInputRef}
          type="file"
          accept="image/*"
          onChange={handleImageUpload}
          className="hidden"
        />

        <div className="relative w-[1010px] px-4">
          <CreateInput value={name} onChange={handleNameChange} placeholder="Name" />
          <div className="text-muted-foreground mt-2 text-right text-sm">
            {name.length}/{MAX_NAME_LENGTH} characters
          </div>
        </div>
      </header>

      <main className="flex flex-1 items-center justify-center">
        <CreateBookClubButton name={name} imageBase64={imageBase64} />
      </main>
    </div>
  );
}
