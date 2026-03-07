'use client';

import * as React from 'react';
import useEmblaCarousel, { type UseEmblaCarouselType } from 'embla-carousel-react';
import { ArrowLeft, ArrowRight } from 'lucide-react';
import { cva, type VariantProps } from 'class-variance-authority';

import { cn } from '@/lib/utils';
import { Button } from '@/components/ui/button';

//
// CVA definitions
//

const carouselVariants = cva('relative', {
  variants: {
    orientation: {
      horizontal: '',
      vertical: '',
    },
  },
  defaultVariants: {
    orientation: 'horizontal',
  },
});

const carouselContentVariants = cva('flex', {
  variants: {
    orientation: {
      horizontal: '',
      vertical: '-mt-4 flex-col',
    },
  },
  defaultVariants: {
    orientation: 'horizontal',
  },
});

const carouselItemVariants = cva('shrink-0 w-full', {
  variants: {
    rows: {
      1: 'grid-rows-1',
      2: 'grid-rows-2',
      3: 'grid-rows-3',
      4: 'grid-rows-4',
      5: 'grid-rows-5',
      6: 'grid-rows-6',
      7: 'grid-rows-7',
      8: 'grid-rows-8',
    },
    columns: {
      1: 'grid-cols-1',
      2: 'grid-cols-2',
      3: 'grid-cols-3',
      4: 'grid-cols-4',
      5: 'grid-cols-5',
      6: 'grid-cols-6',
      7: 'grid-cols-7',
      8: 'grid-cols-8',
    },
  },
  defaultVariants: {
    rows: 1,
    columns: 1,
  },
});

const carouselIndicatorVariants = cva('rounded-full transition-colors', {
  variants: {
    intent: {
      primary: '', // base styling if needed
      neutral: '',
    },
    active: {
      true: '', // empty, handled by compoundVariants
      false: '',
    },
    size: {
      sm: 'w-2 h-2',
      md: 'w-4 h-4',
      lg: 'w-6 h-6',
    },
  },
  compoundVariants: [
    { intent: 'primary', active: true, className: 'bg-blue-500' },
    { intent: 'primary', active: false, className: 'bg-gray-300' },
    { intent: 'neutral', active: true, className: 'bg-button-neutral' },
    { intent: 'neutral', active: false, className: 'bg-button-disabled-neutral' },
  ],
  defaultVariants: {
    intent: 'primary',
    active: false,
    size: 'md',
  },
});

const carouselButtonVariants = cva('size-8 rounded-full', {
  variants: {
    orientation: {
      horizontalPrev: 'top-1/2 -left-12 -translate-y-1/2 absolute',
      horizontalNext: 'top-1/2 -right-12 -translate-y-1/2 absolute',
      verticalPrev: '-top-12 left-1/2 -translate-x-1/2 rotate-90 absolute',
      verticalNext: '-bottom-12 left-1/2 -translate-x-1/2 rotate-90 absolute',
      below: '', // no absolute, will be handled by flex container
    },
  },
  defaultVariants: {
    orientation: 'horizontalPrev',
  },
});

//
// Types
//
type BookProgressCarouselApi = UseEmblaCarouselType[1];
type UseBookProgressCarouselParameters = Parameters<typeof useEmblaCarousel>;
type BookProgressCarouselOptions = UseBookProgressCarouselParameters[0];
type BookProgressCarouselPlugin = UseBookProgressCarouselParameters[1];

type BookProgressCarouselProps = {
  opts?: BookProgressCarouselOptions;
  plugins?: BookProgressCarouselPlugin;
  orientation?: 'horizontal' | 'vertical';
  setApi?: (api: BookProgressCarouselApi) => void;
};

type BookProgressCarouselContextProps = {
  carouselRef: ReturnType<typeof useEmblaCarousel>[0];
  api: ReturnType<typeof useEmblaCarousel>[1];
  scrollPrev: () => void;
  scrollNext: () => void;
  canScrollPrev: boolean;
  canScrollNext: boolean;
} & BookProgressCarouselProps;

//
// Context
//
const BookProgressCarouselContext = React.createContext<
  (BookProgressCarouselContextProps & { selectedIndex: number }) | null
>(null);

function useBookProgressCarousel() {
  const context = React.useContext(BookProgressCarouselContext);

  if (!context) {
    throw new Error('useBookProgressCarousel must be used within a <BookProgressCarousel />');
  }

  return context;
}

//
// Components
//
function BookProgressCarousel({
  orientation = 'horizontal',
  opts,
  setApi,
  plugins,
  className,
  children,
  ...props
}: React.ComponentProps<'div'> &
  BookProgressCarouselProps &
  VariantProps<typeof carouselVariants>) {
  const [carouselRef, api] = useEmblaCarousel(
    {
      ...opts,
      axis: orientation === 'horizontal' ? 'x' : 'y',
    },
    plugins,
  );
  const [canScrollPrev, setCanScrollPrev] = React.useState(false);
  const [canScrollNext, setCanScrollNext] = React.useState(false);

  const [selectedIndex, setSelectedIndex] = React.useState(0);

  const onSelect = React.useCallback((api: BookProgressCarouselApi) => {
    if (!api) return;
    setCanScrollPrev(api.canScrollPrev());
    setCanScrollNext(api.canScrollNext());
    setSelectedIndex(api.selectedScrollSnap());
  }, []);

  const scrollPrev = React.useCallback(() => {
    api?.scrollPrev();
  }, [api]);

  const scrollNext = React.useCallback(() => {
    api?.scrollNext();
  }, [api]);

  const handleKeyDown = React.useCallback(
    (event: React.KeyboardEvent<HTMLDivElement>) => {
      if (event.key === 'ArrowLeft') {
        event.preventDefault();
        scrollPrev();
      } else if (event.key === 'ArrowRight') {
        event.preventDefault();
        scrollNext();
      }
    },
    [scrollPrev, scrollNext],
  );

  React.useEffect(() => {
    if (!api || !setApi) return;
    setApi(api);
  }, [api, setApi]);

  React.useEffect(() => {
    if (!api) return;
    onSelect(api);
    api.on('reInit', onSelect);
    api.on('select', onSelect);

    return () => {
      api?.off('select', onSelect);
    };
  }, [api, onSelect]);

  return (
    <BookProgressCarouselContext.Provider
      value={{
        carouselRef,
        api: api,
        opts,
        orientation: orientation || (opts?.axis === 'y' ? 'vertical' : 'horizontal'),
        scrollPrev,
        scrollNext,
        canScrollPrev,
        canScrollNext,
        selectedIndex,
      }}
    >
      <div
        onKeyDownCapture={handleKeyDown}
        className={cn(carouselVariants({ orientation }), className)}
        role="region"
        aria-roledescription="carousel"
        data-slot="carousel"
        {...props}
      >
        {children}
      </div>
    </BookProgressCarouselContext.Provider>
  );
}

function BookProgressCarouselContent({ className, ...props }: React.ComponentProps<'div'>) {
  const { carouselRef, orientation } = useBookProgressCarousel();

  return (
    <div ref={carouselRef} className="overflow-hidden" data-slot="carousel-content">
      <div className={cn(carouselContentVariants({ orientation }), className)} {...props}>
        {props.children}
      </div>
    </div>
  );
}

type BookProgressCarouselItemProps = React.ComponentProps<'div'> &
  VariantProps<typeof carouselItemVariants>;
function BookProgressCarouselItem({
  className,
  rows,
  columns,
  children,
  ...props
}: BookProgressCarouselItemProps) {
  return (
    <div
      role="group"
      aria-roledescription="slide"
      data-slot="carousel-item"
      className={cn('min-w-0 shrink-0 grow-0 basis-full', className)}
      {...props}
    >
      <div
        className={cn(
          'grid h-full place-items-center gap-6',
          carouselItemVariants({ rows, columns }),
        )}
      >
        {children}
      </div>
    </div>
  );
}

function BookProgressCarouselArrows({
  className,
  ...props
}: {
  className?: string;
  children?: React.ReactNode;
}) {
  return <div className={cn('mt-4 flex justify-center gap-4', className)} {...props} />;
}

type BookProgressCarouselIndicatorProps = {
  isActive?: boolean;
  intent?: 'primary' | 'neutral';
  size?: 'sm' | 'md' | 'lg';
};

function BookProgressCarouselIndicators({
  children,
  className,
}: {
  children: React.ReactElement<BookProgressCarouselIndicatorProps>[]; // <-- only elements that accept isActive
  className?: string;
}) {
  const { selectedIndex } = useBookProgressCarousel();

  return (
    <div className={cn('flex items-center justify-center gap-2', className)}>
      {React.Children.map(children, (child, idx) =>
        React.isValidElement<BookProgressCarouselIndicatorProps>(child)
          ? React.cloneElement(child, {
              isActive: idx === selectedIndex,
            })
          : child,
      )}
    </div>
  );
}

function BookProgressCarouselIndicator({
  intent,
  isActive,
  size,
}: BookProgressCarouselIndicatorProps) {
  return <span className={carouselIndicatorVariants({ intent, active: !!isActive, size })} />;
}

function BookProgressCarouselPrevious({
  position = 'around',
  className,
  variant = 'outline',
  size = 'icon',
  ...props
}: { position?: 'around' | 'below' } & React.ComponentProps<typeof Button>) {
  const { orientation, scrollPrev, canScrollPrev } = useBookProgressCarousel();
  const orientationKey =
    position === 'below'
      ? 'below'
      : orientation === 'horizontal'
        ? 'horizontalPrev'
        : 'verticalPrev';
  return (
    <Button
      data-slot="carousel-next"
      variant={variant}
      size={size}
      className={cn(carouselButtonVariants({ orientation: orientationKey }), className)}
      disabled={!canScrollPrev}
      onClick={scrollPrev}
      {...props}
    >
      {' '}
      <ArrowLeft /> <span className="sr-only">Previous slide</span>{' '}
    </Button>
  );
}

function BookProgressCarouselNext({
  position = 'around',
  className,
  variant = 'outline',
  size = 'icon',
  ...props
}: { position?: 'around' | 'below' } & React.ComponentProps<typeof Button>) {
  const { orientation, scrollNext, canScrollNext } = useBookProgressCarousel();
  const orientationKey =
    position === 'below'
      ? 'below'
      : orientation === 'horizontal'
        ? 'horizontalNext'
        : 'verticalNext';
  return (
    <Button
      data-slot="carousel-next"
      variant={variant}
      size={size}
      className={cn(carouselButtonVariants({ orientation: orientationKey }), className)}
      disabled={!canScrollNext}
      onClick={scrollNext}
      {...props}
    >
      {' '}
      <ArrowRight /> <span className="sr-only">Next slide</span>{' '}
    </Button>
  );
}

export {
  type BookProgressCarouselApi,
  BookProgressCarousel,
  BookProgressCarouselContent,
  BookProgressCarouselItem,
  BookProgressCarouselArrows,
  BookProgressCarouselIndicators,
  BookProgressCarouselIndicator,
  BookProgressCarouselPrevious,
  BookProgressCarouselNext,
};
