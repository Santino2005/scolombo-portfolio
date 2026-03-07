package tome.bookClub.book

import org.jetbrains.annotations.NotNull
import tome.UserProfileDTO
import tome.bookClub.book.vote.ResponseVoteDto
import tome.library.status.LibraryBookStatusType
import java.util.UUID

data class CreateBookClubBookDto(
    @field:NotNull val bookClubId: UUID,
    @field:NotNull val bookId: UUID,
    val status: BookClubBookStatus = BookClubBookStatus.PROPOSED,
)

data class ResponseBookClubBookDto(
    val bookClubId: UUID,
    val bookId: UUID,
    val coverUrl: String,
    val status: BookClubBookStatus,
    val vote: Boolean?,
    val votes: Map<String, List<ResponseVoteDto>>?,
    val membersReadingStatus: Map<LibraryBookStatusType, List<UserProfileDTO>>?,
)

data class UpdateBookClubBookDto(
    @field:NotNull val status: BookClubBookStatus,
)
