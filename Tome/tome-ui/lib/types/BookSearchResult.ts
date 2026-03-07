export type Author = {
  id: string;
  name: string;
  surname: string;
};
export type Tag = {
  id: string;
  name: string;
};
export type Publisher = {
  id: string;
  name: string;
};

export type Language = {
  id: string;
  name: string;
};

export type LibraryInfo = {
  readingStatus: 'New_Status' | 'WANT_TO_READ' | 'READING' | 'READ' | 'DNF';
  startedAt?: string | null;
  finishedAt?: string | null;
  currentPage: number;
};

export type BookSearchResult = {
  id: string;
  title: string;
  authors: string[];
  coverUrl: string;
};

export type LibraryBookApiDTO = {
  book: {
    id: string;
    title: string;
    isbn: string;
    url: string;
    pages: number;
    releaseDate: string;
    synopsis: string;
    language: Language;
    publisher: Publisher;
    authors: Author[];
    tags: Tag[];
    libraryBookStatusDTO: LibraryInfo;
  };
  title: string;
  authors: Author[];
  coverUrl: string;
  pages: number;
  currentPage: number;
};

export interface LibraryPageDTO {
  content: LibraryBookApiDTO[];
  last: boolean;
}
