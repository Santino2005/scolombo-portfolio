package tome.bookClub.book.vote

import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tome.Auth0ManagementService
import tome.UserProfileDTO
import java.util.UUID

@RestController
@RequestMapping("/book-clubs")
class BookClubBookVoteController(
    val service: BookClubBookVoteService,
    val auth0Service: Auth0ManagementService,
) {
    @PostMapping("/{bookClubId}/books/{bookId}/votes")
    fun vote(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable bookClubId: UUID,
        @PathVariable bookId: UUID,
        @Valid @RequestBody dto: VoteDto,
    ): ResponseVoteDto {
        val vote = service.create(bookClubId, bookId, jwt.subject, dto.hasAccepted)
        return convertToResponseDto(vote)
    }

    @GetMapping("/{bookClubId}/books/{bookId}/votes/me")
    fun readVote(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable bookClubId: UUID,
        @PathVariable bookId: UUID,
    ): ResponseVoteDto? {
        val vote = service.read(bookClubId, bookId, jwt.subject) ?: return null
        return convertToResponseDto(vote)
    }

    @GetMapping("/{bookClubId}/books/{bookId}/votes")
    fun readBookClubBookVotes(
        @PathVariable bookClubId: UUID,
        @PathVariable bookId: UUID,
    ): Map<String, List<ResponseVoteDto>> {
        val votes = service.readAll(bookClubId, bookId)
        return votes.mapValues { (_, voteList) ->
            voteList.map { vote ->
                convertToResponseDto(vote)
            }
        }
    }

    @PatchMapping("/{bookClubId}/books/{bookId}/votes/me")
    fun modifyVote(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable bookClubId: UUID,
        @PathVariable bookId: UUID,
        @Valid @RequestBody dto: VoteDto,
    ): ResponseVoteDto {
        val vote = service.updateVote(bookClubId, bookId, jwt.subject, dto.hasAccepted)
        return convertToResponseDto(vote)
    }

    private fun convertToResponseDto(vote: BookClubBookVote): ResponseVoteDto {
        val getUserResponse = auth0Service.getUserById(vote.id.userId)
        val user: UserProfileDTO = requireNotNull(getUserResponse.body) { "Error getting user profile data." }
        return ResponseVoteDto(
            bookClubId = vote.id.bookClubId,
            bookId = vote.id.bookId,
            user = user,
            hasAccepted = vote.hasAccepted,
            bookIsWinner = false,
        )
    }

    private fun convertToResponseDto(voteResult: CreateVoteResult): ResponseVoteDto {
        val vote = voteResult.vote
        val getUserResponse = auth0Service.getUserById(vote.id.userId)
        val user: UserProfileDTO =
            requireNotNull(getUserResponse.body) { "Error getting user profile data." }
        return ResponseVoteDto(
            bookClubId = vote.id.bookClubId,
            bookId = vote.id.bookId,
            user = user,
            hasAccepted = vote.hasAccepted,
            bookIsWinner = voteResult.bookIsWinner,
        )
    }
}
