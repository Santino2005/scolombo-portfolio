package tome.bookClub.book.vote

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository interface BookClubBookVoteRepo : JpaRepository<BookClubBookVote, BookClubBookVoteId> {
    @Suppress("FunctionName")
    fun findById_BookClubIdAndId_BookId(
        bookClubId: UUID,
        bookId: UUID,
    ): List<BookClubBookVote>

    @Suppress("FunctionName")
    fun countById_BookClubIdAndId_BookIdAndHasAccepted(
        bookClubId: UUID,
        bookId: UUID,
        hasAccepted: Boolean,
    ): Long

    @Suppress("FunctionName")
    fun countById_BookClubIdAndId_BookId(
        bookClubId: UUID,
        bookId: UUID,
    ): Long

    fun findByIdBookClubIdAndIdBookIdAndIdUserId(
        bookClubId: UUID,
        bookId: UUID,
        userId: String,
    ): BookClubBookVote?
}
