package tome.bookClub.reader

import tome.bookClub.BookClub
import tome.bookClub.book.ResponseBookClubBookDto
import java.util.Base64

fun toBookClubDetailsDTO(
    bookClub: BookClub,
    currentBook: ResponseBookClubBookDto? = null,
): BookClubDetailsDTO {
    val base64Image =
        bookClub.imgBlob?.let { Base64.getEncoder().encodeToString(it) }?.let {
            "data:image/jpeg;base64,$it"
        }

    return BookClubDetailsDTO(
        id = bookClub.id,
        details = bookClub.name,
        imageBase64 = base64Image,
        currentBook = currentBook,
    )
}
