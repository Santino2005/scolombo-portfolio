package tome.library.dto

import tome.books.author.Author
import tome.books.dto.BookDTO

data class LibraryBookDTO(
    val book: BookDTO,
    val title: String,
    val authors: List<Author>,
    val coverUrl: String,
    val pages: Int,
    val currentPage: Int,
)
