package tome.bookClub.book.vote

import org.jetbrains.annotations.NotNull
import tome.UserProfileDTO
import java.util.UUID

data class VoteDto(
    @field:NotNull val hasAccepted: Boolean,
)

data class ResponseVoteDto(
    val bookClubId: UUID,
    val bookId: UUID,
    val user: UserProfileDTO,
    val hasAccepted: Boolean?,
    val bookIsWinner: Boolean,
)
