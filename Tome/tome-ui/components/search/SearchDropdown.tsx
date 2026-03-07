import type { BookData } from '@/lib/types/BookData';
import { SearchBooksContainer } from '../books/search/SearchBooksContainer';

interface SearchDropdownProps {
  books: BookData[];
  searchTerm: string;
  isVisible: boolean;
  isLoading: boolean;
  error?: string | null;
  onBookSelect: (book: BookData) => void;
  onClose: () => void;
  totalElements: number;
}

export function SearchDropdown({
  books,
  searchTerm,
  isVisible,
  isLoading,
  error,
  onBookSelect,
  onClose,
  totalElements,
}: SearchDropdownProps) {
  if (!isVisible) return null;

  return (
    <>
      <div
        className="fixed inset-0 z-40"
        style={{
          top: '80px',
          pointerEvents: 'auto',
        }}
        onClick={onClose}
      />

      <div className="bg-background border-border absolute top-full right-0 left-0 z-50 mt-1 max-h-96 overflow-hidden rounded-lg border shadow-lg">
        {isLoading && (
          <div className="flex items-center justify-center py-8">
            <div className="border-primary mr-3 h-6 w-6 animate-spin rounded-full border-b-2"></div>
            <span className="text-muted-foreground">Searching...</span>
          </div>
        )}

        {error && !isLoading && (
          <div className="px-4 py-6 text-center">
            <div className="text-muted-foreground mb-2">
              {error === 'No se encontraron resultados para tu búsqueda' ? (
                <div>
                  <p className="text-foreground mb-1 font-medium">No results</p>
                  <p className="text-sm">No results were found for your search</p>
                </div>
              ) : (
                <div>
                  <p className="text-destructive mb-1 font-medium">Error</p>
                  <p className="text-sm">{error}</p>
                </div>
              )}
            </div>
          </div>
        )}

        {!isLoading && !error && books.length > 0 && (
          <div className="p-4">
            <div className="text-muted-foreground mb-1 text-sm">
              Showing {books.length} of {totalElements} result
              {totalElements !== 1 ? 's' : ''} for &ldquo;{searchTerm}
              &rdquo;
            </div>

            <div className="max-h-60 overflow-y-auto">
              <SearchBooksContainer books={books} searchParam={searchTerm} />
            </div>
          </div>
        )}

        {!isLoading && !error && books.length === 0 && searchTerm && (
          <div className="px-4 py-6 text-center">
            <p className="text-foreground mb-1 font-medium">No results</p>
            <p className="text-muted-foreground text-sm">
              No books were found for &ldquo;{searchTerm}&rdquo;
            </p>
          </div>
        )}
      </div>
    </>
  );
}
