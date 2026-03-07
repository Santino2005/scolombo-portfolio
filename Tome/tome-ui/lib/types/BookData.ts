export type Author = {
  id: string;
  fullName: string;
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

export type BookSearchData = {
  id: string;
  title: string;
  author: Author[];
  coverUrl: string;
};

export type BookData = {
  id: string;
  title: string;
  authors: Author[];
  url: string;
  releaseDate?: string;
  pages: number;
  synopsis?: string;
  publisher?: Publisher;
  isbn?: string;
  language?: Language;
  tags?: Tag[];
  libraryBookStatusDTO?: LibraryInfo | null;
};
export type LibraryEntry = {
  book: BookData;
  pages: number;
  currentPage: number;
};

export type LibraryInfo = {
  readingStatus: 'New_Status' | 'WANT_TO_READ' | 'READING' | 'READ' | 'DNF';
  startedAt?: Date;
  finishedAt?: Date;
  currentPage: number;
};
