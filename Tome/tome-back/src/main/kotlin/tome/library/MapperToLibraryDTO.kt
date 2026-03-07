package tome.library

import tome.books.toBookDTO
import tome.library.dto.LibraryBookDTO
import tome.library.dto.LibraryBookStatusTypeDTO

fun toLibraryBookStatus(libraryBook: LibraryBook): LibraryBookStatusTypeDTO =
    LibraryBookStatusTypeDTO(
        readingStatus = libraryBook.status,
        currentPage = libraryBook.currentPage,
        startedAt = libraryBook.startedAt?.toString(),
        finishedAt = libraryBook.finishedAt?.toString(),
    )

fun toLibraryBookDTO(libraryBook: LibraryBook): LibraryBookDTO =
    LibraryBookDTO(
        book = toBookDTO(libraryBook.book, libraryBook),
        title = libraryBook.book.title,
        authors = libraryBook.book.authors,
        coverUrl = libraryBook.book.coverUrl,
        pages = libraryBook.book.pages,
        currentPage = libraryBook.currentPage,
    )

fun convertToLibraryBook(libraryBook: List<LibraryBook>): List<LibraryBookDTO> = libraryBook.map(::toLibraryBookDTO)
