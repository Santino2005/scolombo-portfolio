package tome.books.dto

import tome.books.author.Author
import tome.books.languageApi.Language
import tome.books.publisherApi.Publisher
import tome.books.tag.Tag
import tome.library.dto.LibraryBookStatusDTO
import java.time.LocalDate
import java.util.UUID

data class BookDTO(
    val id: UUID,
    val title: String,
    val isbn: String,
    val url: String,
    val pages: Int,
    val releaseDate: LocalDate,
    val synopsis: String,
    val language: Language,
    val publisher: Publisher,
    val authors: List<Author>,
    val tags: List<Tag>,
    val libraryBookStatusDTO: LibraryBookStatusDTO?,
)
