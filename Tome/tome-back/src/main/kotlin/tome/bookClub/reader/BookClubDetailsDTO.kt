package tome.bookClub.reader

import tome.bookClub.book.ResponseBookClubBookDto
import java.util.UUID

data class BookClubDetailsDTO(
    val id: UUID?,
    val details: String,
    val imageBase64: String?,
    val currentBook: ResponseBookClubBookDto?,
)
