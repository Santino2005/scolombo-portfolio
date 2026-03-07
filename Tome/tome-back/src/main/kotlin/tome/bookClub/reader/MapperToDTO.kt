package tome.bookClub.reader

import tome.bookClub.BookClub

fun toBookClubDTO(bookClub: BookClub): BookClubDTO = BookClubDTO(id = bookClub.id)
