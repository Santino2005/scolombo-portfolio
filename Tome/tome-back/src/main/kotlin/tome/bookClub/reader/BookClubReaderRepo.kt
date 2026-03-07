package tome.bookClub.reader

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tome.bookClub.BookClub
import java.util.UUID

@Repository
interface BookClubReaderRepo : JpaRepository<BookClubReader, UUID> {
    fun existsByBookClubAndUserIdAndDeletedFalse(
        bookClub: BookClub,
        userId: String,
    ): Boolean

    fun findByUserIdAndDeletedFalse(userId: String): List<BookClubReader>

    @Suppress("FunctionName")
    fun findByBookClub_IdAndDeletedFalse(bookClubId: UUID): List<BookClubReader>

    @Suppress("FunctionName")
    fun countByBookClub_Id(bookClubId: UUID): Long

    @Suppress("FunctionName")
    fun findByUserIdAndBookClub_IdAndDeletedFalse(
        userId: String,
        bookClubId: UUID,
    ): BookClubReader?
}
