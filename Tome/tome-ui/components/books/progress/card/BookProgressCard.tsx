'use client';

import type { Author, LibraryEntry } from '@/lib/types/BookData';

import { Button } from '@/components/ui/button';
import { Progress } from '@/components/ui/progress';
import { Input } from '@/components/ui/input';
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { useCallback, useEffect, useState } from 'react';
import { toast } from 'sonner';
import { BookCover } from '@/components/books/BookCover';
import Link from 'next/link';

interface BookProgressCardProps {
  libraryEntry: LibraryEntry;
  onStatusChange: (bookId: string) => void;
}

function BookProgressCard({ libraryEntry, onStatusChange }: BookProgressCardProps) {
  const [amountType, setAmountType] = useState<'#' | '%'>('#');
  const [value, setValue] = useState<string>(libraryEntry.currentPage?.toString() ?? '0');
  const [max, setMax] = useState(0);
  const [step, setStep] = useState<number>(1);
  const [isLoading, setIsLoading] = useState(false);
  const [isSaving, setIsSaving] = useState(false);

  const totalPages = libraryEntry.book.pages ?? 0;

  // Calcula porcentaje de páginas
  const getValuePercentage = useCallback(
    (pages: number) => (totalPages ? Number(((pages / totalPages) * 100).toFixed(1)) : 0),
    [totalPages],
  );

  // Convierte porcentaje a páginas
  const getPercentageValue = useCallback(
    (percentage: number) => (totalPages ? Math.floor((percentage / 100) * totalPages) : 0),
    [totalPages],
  );

  useEffect(() => {
    const readPages = libraryEntry.currentPage ?? 0;

    if (amountType === '#') {
      setMax(totalPages);
      setValue(readPages.toString());
      setStep(1);
    } else {
      setMax(100);
      setValue(getValuePercentage(readPages).toString());
      setStep(Number((100 / totalPages).toFixed(8) || 1));
    }
  }, [libraryEntry, amountType, getValuePercentage, totalPages]);

  const putCurrentPageAndState = useCallback(
    async (bookId: string, currentPage: number, status: string) => {
      if (currentPage < 0 || currentPage > totalPages) {
        return;
      }
      setIsSaving(true);
      try {
        const res = await fetch('/api/library', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ bookId, currentPage, status }),
        });

        if (!res.ok) throw new Error('Failed to update');

        const data = await res.json();
        const temp = data.book as LibraryEntry;
        libraryEntry.currentPage = temp.currentPage;
        toast.success('Saved Progress');
      } catch {
        toast.error('Saving progress error');
      } finally {
        setIsSaving(false);
      }
    },
    [libraryEntry, totalPages],
  );

  const handleSetStatus = useCallback(
    async (newStatus: 'READ' | 'DNF') => {
      setIsLoading(true);
      try {
        const body = {
          bookId: libraryEntry.book.id,
          readingStatus: newStatus,
        };
        const res = await fetch('/api/library/status', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(body),
        });
        if (res.ok) {
          toast.success(`Book status updated to "${newStatus}".`);
          onStatusChange(libraryEntry.book.id);
        } else {
          toast.error('Error updating book status.');
        }
      } catch (error) {
        console.error('[BookProgressCard] handleSetStatus error:', error);
        toast.error('Network error: could not connect to the server.');
      } finally {
        setIsLoading(false);
      }
    },
    [libraryEntry.book.id, onStatusChange],
  );

  const finishBookFunction = () => handleSetStatus('READ');
  const dnfBookFunction = () => handleSetStatus('DNF');

  const myHandler = (type: '#' | '%') => {
    setAmountType(type);
  };

  useEffect(() => {
    if (Number(value) === libraryEntry.currentPage && amountType === '#') return;
    if (value === '') return;

    const timeout = setTimeout(() => {
      const currentPage = amountType === '#' ? Number(value) : getPercentageValue(Number(value));
      if (currentPage !== libraryEntry.currentPage) {
        putCurrentPageAndState(libraryEntry.book.id, currentPage, 'READING');
      }
    }, 1500);

    return () => clearTimeout(timeout);
  }, [value, libraryEntry, getPercentageValue, amountType, putCurrentPageAndState]);

  return (
    <div className="gap-0[var(--spacing-m)] flex h-[189px] w-[488px] flex-row rounded-md bg-[var(--ds-neutral-200)]">
      {/* Book cover */}
      <Link href={`/books/${libraryEntry.book.id}`} className="group block flex-shrink-0">
        <div
          className="relative overflow-hidden rounded-lg shadow-lg"
          style={{ width: '125px', height: '189px' }}
        >
          <BookCover
            url={libraryEntry.book.url}
            title={libraryEntry.book.title}
            className="cursor-pointer rounded-md object-cover transition-transform hover:scale-105"
            sizes="125px"
          />
        </div>
      </Link>

      {/* Content */}
      <div className="flex h-full min-w-0 flex-1 flex-col justify-between">
        {/* Title + authors */}
        <div className="ml-3 min-w-0 pt-3 pr-2">
          <h3 className="block w-full truncate text-xl font-bold">{libraryEntry.book.title}</h3>
          <h4 className="text-muted-foreground block w-full truncate text-lg">
            {libraryEntry.book.authors.map((a: Author) => a.fullName + ' ' + a.surname).join(', ')}
          </h4>
        </div>

        {/* Bottom section */}
        <div className="bg-muted/20 rounded-b-md">
          {/* Controls row — allows wrapping */}
          <div className="flex w-full flex-wrap items-center justify-end gap-[var(--spacing-s)] p-2">
            <Input
              type="text"
              value={value}
              disabled={isSaving}
              onChange={(e) => {
                const val = e.target.value;
                if (/^\d*$/.test(val)) {
                  const num = Number(val);
                  if (val === '' || (num >= 0 && num <= max)) {
                    setValue(val);
                  }
                }
              }}
              onBlur={() => {
                if (value === '') {
                  const readPages = libraryEntry.currentPage ?? 0;
                  if (amountType === '#') {
                    setValue(readPages.toString());
                  } else {
                    setValue(getValuePercentage(readPages).toString());
                  }
                }
              }}
              className="bg-background h-9 w-[60px]"
            />

            <Select defaultValue="#" onValueChange={myHandler}>
              <SelectTrigger className="bg-background h-9 w-[63px]">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectGroup>
                  <SelectLabel>Type</SelectLabel>
                  <SelectItem value="#">#</SelectItem>
                  <SelectItem value="%">%</SelectItem>
                </SelectGroup>
              </SelectContent>
            </Select>

            <Button
              onClick={finishBookFunction}
              variant="neutral"
              disabled={isLoading}
              size="lg"
              className="h-9 px-3"
            >
              Finish
            </Button>

            <Button
              onClick={dnfBookFunction}
              variant="neutral_outline"
              disabled={isLoading}
              size="lg"
              className="h-9 px-3"
            >
              DNF
            </Button>
          </div>

          {/* Progress bar */}
          <div className="w-full px-2 pb-2">
            <Progress
              value={getValuePercentage(
                amountType === '#' ? Number(value) : getPercentageValue(Number(value)),
              )}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export default BookProgressCard;
