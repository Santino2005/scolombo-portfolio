package tome.bookClub.book

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tome.bookClub.book.vote.BookClubBookVoteController
import tome.bookClub.book.vote.VoteDto
import tome.bookClub.reader.BookClubReaderService
import tome.library.LibraryBookService
import java.util.UUID

@RestController
@RequestMapping("/book-clubs")
class BookClubBookController(
    val service: BookClubBookService,
    val voteController: BookClubBookVoteController,
    val bookClubReaderService: BookClubReaderService,
    val libraryBookService: LibraryBookService,
) {
    @PostMapping("/{bookClubId}/books/{bookId}")
    fun create(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable bookClubId: UUID,
        @PathVariable bookId: UUID,
    ): ResponseBookClubBookDto {
        val book = service.create(jwt.subject, bookClubId, bookId)
        val vote = voteController.vote(jwt, bookClubId, bookId, VoteDto(true))
        return convertToBookClubBookDto(book, vote.hasAccepted, includeVotes = true, includeMembersStatus = false)
    }

    @GetMapping("/{bookClubId}/books/{bookId}")
    fun read(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable bookClubId: UUID,
        @PathVariable bookId: UUID,
    ): ResponseBookClubBookDto {
        val it = service.read(bookClubId, bookId)
        return convertToBookClubBookDto(
            it,
            (voteController.readVote(jwt, requireNotNull(it.id.bookClubId), requireNotNull(it.id.bookId)))?.hasAccepted,
            includeVotes = true,
            includeMembersStatus = false,
        )
    }

    @GetMapping("/{bookClubId}/books")
    fun readAll(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestParam(defaultValue = "PROPOSED") status: BookClubBookStatus,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "book") sort: String,
        @RequestParam(defaultValue = "asc") direction: String,
        @PathVariable bookClubId: UUID,
    ): Page<ResponseBookClubBookDto> {
        val sorting: Sort = Sort.by(Sort.Direction.fromString(direction), sort)
        val pageable: Pageable = PageRequest.of(page, size, sorting)
        val bookPage: Page<BookClubBook> = service.readAll(status, bookClubId, pageable)
        return bookPage.map {
            convertToBookClubBookDto(
                it,
                (
                    voteController.readVote(
                        jwt,
                        requireNotNull(it.id.bookClubId),
                        requireNotNull(it.id.bookId),
                    )
                )?.hasAccepted,
                includeVotes = true,
                includeMembersStatus = false,
            )
        }
    }

    @GetMapping("/{bookClubId}/books/current")
    fun readCurrent(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable bookClubId: UUID,
    ): ResponseBookClubBookDto? {
        val book =
            service.readCurrent(bookClubId, jwt.subject)
                ?: return null
        return convertToBookClubBookDto(
            book,
            null,
            includeVotes = false,
            includeMembersStatus = true,
        )
    }

    @PatchMapping("{bookClubId}/books/{bookId}")
    fun update(
        @PathVariable bookClubId: UUID,
        @PathVariable bookId: UUID,
        @AuthenticationPrincipal jwt: Jwt,
        @Valid @RequestBody dto: UpdateBookClubBookDto,
    ): ResponseBookClubBookDto {
        val book = service.updateState(bookClubId, bookId, dto.status)
        return convertToBookClubBookDto(
            book,
            (
                voteController.readVote(
                    jwt,
                    requireNotNull(book.id.bookClubId),
                    requireNotNull(book.id.bookId),
                )
            )?.hasAccepted,
            includeVotes = true,
            includeMembersStatus = true,
        )
    }

    private fun convertToBookClubBookDto(
        clubBook: BookClubBook,
        vote: Boolean?,
        includeVotes: Boolean = true,
        includeMembersStatus: Boolean = false,
    ): ResponseBookClubBookDto {
        val bookClubId = requireNotNull(clubBook.id.bookClubId) { "BookClubBook must have a valid Book Club" }
        val bookId = requireNotNull(clubBook.id.bookId) { "BookClubBook must have a valid Book" }

        val votes =
            if (includeVotes) {
                voteController.readBookClubBookVotes(bookClubId, bookId)
            } else {
                null
            }

        val membersReadingStatus =
            if (includeMembersStatus) {
                val memberIds = bookClubReaderService.getBookClubMembers(bookClubId)
                libraryBookService.getMembersReadingStatusByBook(bookId, memberIds)
            } else {
                null
            }

        return ResponseBookClubBookDto(
            bookClubId = bookClubId,
            bookId = bookId,
            coverUrl = clubBook.book.coverUrl,
            status = clubBook.status,
            vote = vote,
            votes = votes,
            membersReadingStatus = membersReadingStatus,
        )
    }
}
