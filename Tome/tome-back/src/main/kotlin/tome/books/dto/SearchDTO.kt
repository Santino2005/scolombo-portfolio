package tome.books.dto

import tome.books.author.Author
import java.util.UUID

data class SearchDTO(
    val id: UUID,
    val title: String,
    val author: List<Author>,
    val coverUrl: String,
)
