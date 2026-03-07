class Book {
  public id?: string;
  public title: string;
  public author: string;
  public isbn?: string;
  public pages?: number;
  public publishedDate?: Date;
  public coverUrl?: string;
  public description?: string;
  public genre?: string[];
  public rating?: number;

  constructor(
    title: string,
    author: string,
    isbn?: string,
    publishedDate?: Date,
    pages?: number,
    coverUrl?: string,
    description?: string,
    genre?: string[],
  ) {
    this.title = title;
    this.author = author;
    this.isbn = isbn;
    this.publishedDate = publishedDate;
    this.pages = pages;
    this.coverUrl = coverUrl;
    this.description = description;
    this.genre = genre || [];
    this.rating = 0;
  }
}
