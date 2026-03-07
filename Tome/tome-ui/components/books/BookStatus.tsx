'use client';
import * as React from 'react';
import { useState, useEffect, useRef, useCallback } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Trash2 } from 'lucide-react';
import { toast } from 'sonner';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { Calendar } from '@/components/ui/calendar';
import { BookOpenText, BookHeart, BookCheck, BookX, Calendar as CalendarIcon } from 'lucide-react';
import type { BookData, LibraryInfo } from '@/lib/types/BookData';
import { format, isAfter, startOfDay, isBefore } from 'date-fns';

type ReadingStatus = LibraryInfo['readingStatus'] | 'NONE';

interface Props {
  book: BookData;
}

export function BookStatus({ book }: Props) {
  const [isLoading, setIsLoading] = useState(false);
  const [currentStatus, setCurrentStatus] = useState<ReadingStatus>(
    book.libraryBookStatusDTO?.readingStatus ?? 'NONE',
  );
  const [startDate, setStartDate] = useState<Date | undefined>(
    book.libraryBookStatusDTO?.startedAt
      ? new Date(book.libraryBookStatusDTO.startedAt)
      : undefined,
  );

  const [finishDate, setFinishDate] = useState<Date | undefined>(
    book.libraryBookStatusDTO?.finishedAt
      ? new Date(book.libraryBookStatusDTO.finishedAt)
      : undefined,
  );

  const [progress, setProgress] = useState<string>(
    book.libraryBookStatusDTO?.currentPage?.toString() ?? '1',
  );
  const [isReadingPopoverOpen, setIsReadingPopoverOpen] = useState(false);
  const [isDnfPopoverOpen, setIsDnfPopoverOpen] = useState(false);
  const [isSaving] = useState(false);
  const [lastSaved, setLastSaved] = useState<string>(
    book.libraryBookStatusDTO?.currentPage?.toString() ?? '1',
  );
  const [isDeleteConfirmOpen, setIsDeleteConfirmOpen] = useState(false);

  const totalPages = book.pages ?? 9999;
  const saveTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  const handleSetStatus = useCallback(
    async (
      newStatus: ReadingStatus,
      options: { currentPage?: number; startedAt?: Date; finishedAt?: Date } = {},
    ) => {
      if (newStatus === 'NONE') return;
      setIsLoading(true);
      try {
        const body: {
          bookId: string;
          readingStatus: ReadingStatus;
          currentPage?: number;
          startedAt?: string;
          finishedAt?: string;
        } = {
          bookId: book.id,
          readingStatus: newStatus,
        };

        if (newStatus === 'READING') {
          body.currentPage = options.currentPage ?? Number(progress);
        }

        if (newStatus === 'DNF') {
          const dateToSet = options.startedAt ?? startDate;
          if (dateToSet) {
            body.startedAt = format(dateToSet, 'yyyy-MM-dd');
          }
        }

        if (newStatus === 'READ') {
          const s = options.startedAt ?? startDate;
          const f = options.finishedAt ?? finishDate;
          if (s) body.startedAt = format(s, 'yyyy-MM-dd');
          if (f) body.finishedAt = format(f, 'yyyy-MM-dd');
        }

        const res = await fetch(`/api/library/status`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(body),
        });

        if (res.ok) {
          setCurrentStatus(newStatus);

          if (options.currentPage) {
            setLastSaved(options.currentPage.toString());
            toast.success(`Saved Progress: page ${options.currentPage}`);
          } else if (options.startedAt || options.finishedAt) {
            toast.success(`Dates saved.`);
          } else {
            toast.success(`Book status updated to "${newStatus}".`);
          }

          if (newStatus === 'READING') setIsReadingPopoverOpen(true);
          if (newStatus === 'DNF') setIsDnfPopoverOpen(true);
        } else {
          toast.error('Error updating book status.');
        }
      } catch (error) {
        console.error('[BookStatus] handleSetStatus error:', error);
        toast.error('Network error: could not connect to the server.');
      } finally {
        setIsLoading(false);
      }
    },
    [book.id, progress, startDate, finishDate],
  );

  const handleDelete = async () => {
    setIsDeleteConfirmOpen(false);
    setIsLoading(true);
    try {
      const res = await fetch(`/api/library/status/${book.id}`, {
        method: 'DELETE',
      });

      if (res.ok) {
        setCurrentStatus('NONE');
        setProgress('1');
        setStartDate(undefined);
        toast.success('Book removed from your library');
      } else {
        toast.error('Error removing the book');
      }
    } catch (error) {
      console.error('[BookStatus] handleDelete error:', error);
      toast.error('Network error: could not connect to the server');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (!isReadingPopoverOpen || currentStatus !== 'READING' || isSaving) return;
    if (saveTimeoutRef.current) clearTimeout(saveTimeoutRef.current);

    saveTimeoutRef.current = setTimeout(() => {
      if (progress !== lastSaved) {
        handleSetStatus('READING', { currentPage: Number(progress) });
        setLastSaved(progress);
      }
    }, 800);

    return () => {
      if (saveTimeoutRef.current) clearTimeout(saveTimeoutRef.current);
    };
  }, [progress, isReadingPopoverOpen, currentStatus, lastSaved, handleSetStatus, isSaving]);

  const handleStartDateSelect = (date: Date | undefined) => {
    if (!date) return;
    const todayStart = startOfDay(new Date());
    if (isAfter(date, todayStart)) {
      toast.error('Start date cannot be in the future');
      return;
    }
    if (finishDate) {
      if (isAfter(startOfDay(date), startOfDay(finishDate))) {
        toast.error('Start date cannot be after finish date');
        return;
      }
    }
    setStartDate(date);
    if (currentStatus === 'DNF') {
      handleSetStatus('DNF', { startedAt: date });
    }
    if (currentStatus === 'READ') {
      handleSetStatus('READ', { startedAt: date });
    }
  };

  const handleFinishDateSelect = (date: Date | undefined) => {
    if (!date) return;
    const todayStart = startOfDay(new Date());
    if (isAfter(date, todayStart)) {
      toast.error('Finish date cannot be in the future');
      return;
    }
    if (startDate) {
      if (isBefore(startOfDay(date), startOfDay(startDate))) {
        toast.error('Finish date cannot be before start date');
        return;
      }
    }
    setFinishDate(date);
    if (currentStatus === 'READ') {
      handleSetStatus('READ', { finishedAt: date });
    }
  };

  const buttons = [
    { status: 'New_Status', label: 'New Status', icon: <BookHeart size={16} /> },
    { status: 'WANT_TO_READ', label: 'Want to read', icon: <BookHeart size={16} /> },
    { status: 'READING', label: 'Reading', icon: <BookOpenText size={16} /> },
    { status: 'READ', label: 'Read', icon: <BookCheck size={16} /> },
    { status: 'DNF', label: 'DNF', icon: <BookX size={16} /> },
  ] as const;

  return (
    <div className="mt-8 flex flex-col gap-4">
      <div className="flex flex-wrap items-center gap-2">
        {/* Botón de eliminar con pop-up */}
        {currentStatus !== 'NONE' && (
          <Popover open={isDeleteConfirmOpen} onOpenChange={setIsDeleteConfirmOpen}>
            <PopoverTrigger asChild>
              <Button
                disabled={isLoading}
                size="icon"
                className="flex h-12 w-12 items-center justify-center border"
                style={{ backgroundColor: '#F4D7D8', borderColor: '#A32E30' }}
              >
                <Trash2 size={18} color="#A32E30" />
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-64">
              <p className="mb-4 text-sm">
                Are you sure you want to remove this book from your library?
              </p>
              <div className="flex justify-end gap-2">
                <Button variant="outline" size="sm" onClick={() => setIsDeleteConfirmOpen(false)}>
                  Cancel
                </Button>
                <Button variant="destructive" size="sm" onClick={handleDelete}>
                  Delete
                </Button>
              </div>
            </PopoverContent>
          </Popover>
        )}

        {/* Botones de estado */}
        {buttons.map(({ status, label, icon }) => (
          <div key={status} className="min-w-[120px] flex-1">
            {status === 'READING' ? (
              <Popover
                open={currentStatus === 'READING' && isReadingPopoverOpen}
                onOpenChange={setIsReadingPopoverOpen}
              >
                <PopoverTrigger asChild>
                  <Button
                    variant={currentStatus === status ? 'default' : 'outline'}
                    onClick={() =>
                      currentStatus !== 'READING'
                        ? handleSetStatus('READING')
                        : setIsReadingPopoverOpen(!isReadingPopoverOpen)
                    }
                    disabled={isLoading}
                    className="w-full justify-center gap-2"
                  >
                    {icon} <span>{label}</span>
                  </Button>
                </PopoverTrigger>
                <PopoverContent className="w-80">
                  <div className="flex flex-col gap-4">
                    <div>
                      <Label htmlFor="progress-input" className="mb-2">
                        Page
                      </Label>
                      <Input
                        id="progress-input"
                        type="number"
                        min={1}
                        max={totalPages}
                        step={1}
                        placeholder={`1-${totalPages}`}
                        value={progress}
                        onChange={(e) => {
                          const val = e.target.value;
                          if (/^\d*$/.test(val)) {
                            const num = Number(val);
                            if (val === '' || (num >= 1 && num <= totalPages)) {
                              setProgress(val);
                            }
                          }
                        }}
                        disabled={isSaving}
                        className="w-full"
                        aria-label="Current page"
                      />
                      <div className="text-muted-foreground mt-2 text-sm">
                        {isSaving
                          ? 'Saving...'
                          : lastSaved !== progress
                            ? 'Unsaved changes'
                            : 'Progress saved'}
                      </div>
                    </div>
                  </div>
                </PopoverContent>
              </Popover>
            ) : status === 'DNF' ? (
              <Popover
                open={currentStatus === 'DNF' && isDnfPopoverOpen}
                onOpenChange={setIsDnfPopoverOpen}
              >
                <PopoverTrigger asChild>
                  <Button
                    variant={currentStatus === status ? 'default' : 'outline'}
                    onClick={() =>
                      currentStatus !== 'DNF'
                        ? handleSetStatus('DNF')
                        : setIsDnfPopoverOpen(!isDnfPopoverOpen)
                    }
                    disabled={isLoading}
                    className="w-full justify-center gap-2"
                  >
                    {icon} <span>{label}</span>
                  </Button>
                </PopoverTrigger>
                <PopoverContent className="w-80">
                  <div className="flex flex-col gap-4">
                    <div>
                      <Label htmlFor="dnf-date-trigger" className="mb-2">
                        Dropping Date
                      </Label>
                      <Popover>
                        <PopoverTrigger asChild>
                          <Button
                            id="dnf-date-trigger"
                            variant={'outline'}
                            className="w-full justify-between text-left font-normal"
                          >
                            <span>{startDate ? format(startDate, 'PPP') : 'Choose date'}</span>
                            <CalendarIcon className="h-4 w-4" />
                          </Button>
                        </PopoverTrigger>
                        <PopoverContent className="w-auto p-0">
                          <Calendar
                            mode="single"
                            selected={startDate}
                            onSelect={handleStartDateSelect}
                            className="rounded-md border shadow-sm"
                            captionLayout="dropdown"
                          />
                        </PopoverContent>
                      </Popover>
                    </div>
                  </div>
                </PopoverContent>
              </Popover>
            ) : status === 'READ' ? (
              <Popover
                open={currentStatus === 'READ' && isDnfPopoverOpen}
                onOpenChange={setIsDnfPopoverOpen}
              >
                <PopoverTrigger asChild>
                  <Button
                    variant={currentStatus === status ? 'default' : 'outline'}
                    onClick={() =>
                      currentStatus !== 'READ'
                        ? handleSetStatus('READ')
                        : setIsDnfPopoverOpen(!isDnfPopoverOpen)
                    }
                    disabled={isLoading}
                    className="w-full justify-center gap-2"
                  >
                    {icon} <span>{label}</span>
                  </Button>
                </PopoverTrigger>
                <PopoverContent className="w-80">
                  <div className="flex flex-col gap-4">
                    <div>
                      <Label htmlFor="started-date-trigger" className="mb-2">
                        Started Date
                      </Label>
                      <Popover>
                        <PopoverTrigger asChild>
                          <Button
                            id="started-date-trigger"
                            variant={'outline'}
                            className="w-full justify-between text-left font-normal"
                          >
                            <span>{startDate ? format(startDate, 'PPP') : 'Choose date'}</span>
                            <CalendarIcon className="h-4 w-4" />
                          </Button>
                        </PopoverTrigger>
                        <PopoverContent className="w-auto p-0">
                          <Calendar
                            mode="single"
                            selected={startDate}
                            onSelect={handleStartDateSelect}
                            className="rounded-md border shadow-sm"
                            captionLayout="dropdown"
                          />
                        </PopoverContent>
                      </Popover>
                      <Label htmlFor="finished-date-trigger" className="mt-4 mb-2">
                        Finished Date
                      </Label>
                      <Popover>
                        <PopoverTrigger asChild>
                          <Button
                            id="finished-date-trigger"
                            variant={'outline'}
                            className="w-full justify-between text-left font-normal"
                          >
                            <span>{finishDate ? format(finishDate, 'PPP') : 'Choose date'}</span>
                            <CalendarIcon className="h-4 w-4" />
                          </Button>
                        </PopoverTrigger>
                        <PopoverContent className="w-auto p-0">
                          <Calendar
                            mode="single"
                            selected={finishDate}
                            onSelect={handleFinishDateSelect}
                            className="rounded-md border shadow-sm"
                            captionLayout="dropdown"
                          />
                        </PopoverContent>
                      </Popover>
                    </div>
                  </div>
                </PopoverContent>
              </Popover>
            ) : (
              <Button
                variant={currentStatus === status ? 'default' : 'outline'}
                onClick={() => handleSetStatus(status)}
                disabled={isLoading}
                className="w-full justify-center gap-2"
              >
                {icon} <span>{label}</span>
              </Button>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
