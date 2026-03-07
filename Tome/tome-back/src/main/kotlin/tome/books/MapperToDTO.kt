package tome.books

import org.springframework.data.domain.Page
import tome.books.dto.BookDTO
import tome.books.dto.SearchDTO
import tome.books.dto.TagsDTO
import tome.library.LibraryBook
import tome.library.dto.LibraryBookDTO
import tome.library.dto.LibraryBookStatusDTO
import java.util.UUID

fun toBookDTO(
    book: Book,
    libraryBook: LibraryBook?,
) = BookDTO(
    book.id ?: UUID.randomUUID(),
    title = book.title,
    isbn = book.isbn,
    url = book.coverUrl,
    pages = book.pages,
    releaseDate = book.releasedDate,
    synopsis = book.synopsis,
    language = book.language,
    publisher = book.publisher,
    authors = book.authors,
    tags = book.tags,
    libraryBookStatusDTO =
        libraryBook?.let {
            LibraryBookStatusDTO(
                readingStatus = it.status,
                currentPage = it.currentPage,
                startedAt = it.startedAt,
                finishedAt = it.finishedAt,
            )
        },
)

fun toSearchDTO(book: Book) =
    SearchDTO(
        book.id ?: UUID.randomUUID(),
        title = book.title,
        author = book.authors,
        coverUrl = book.coverUrl,
    )

fun toTagsDTO(names: List<String>): TagsDTO = TagsDTO(names = names)

fun toSearchLibraryBookDTO(book: LibraryBook) =
    LibraryBookDTO(
        book = toBookDTO(book.book, book),
        title = book.book.title,
        authors = book.book.authors,
        coverUrl = book.book.coverUrl,
        pages = book.book.pages,
        currentPage = book.currentPage,
    )

fun convertSearchDTO(books: Page<Book>): Page<SearchDTO> = books.map(::toSearchDTO)

fun convertSearchLibraryBookDTO(books: List<LibraryBook>?) = books?.map(::toSearchLibraryBookDTO)
