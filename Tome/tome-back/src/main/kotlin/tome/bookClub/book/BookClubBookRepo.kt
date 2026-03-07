package tome.bookClub.book

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BookClubBookRepo : JpaRepository<BookClubBook, BookClubBookId> {
    @Suppress("FunctionName")
    fun findByBookClub_IdAndBook_Id(
        bookClubId: UUID,
        bookId: UUID,
    ): BookClubBook?

    @Suppress("FunctionName")
    fun findByBookClub_IdAndStatus(
        bookClubId: UUID,
        status: BookClubBookStatus,
        pageable: Pageable,
    ): Page<BookClubBook>

    @Suppress("FunctionName")
    fun findFirstByBookClub_IdAndStatus(
        bookClubId: UUID,
        status: BookClubBookStatus,
    ): BookClubBook?
}
