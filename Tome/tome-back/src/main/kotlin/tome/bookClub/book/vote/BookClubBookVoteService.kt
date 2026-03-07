package tome.bookClub.book.vote

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tome.bookClub.book.BookClubBookService
import tome.bookClub.book.BookClubBookStatus
import tome.bookClub.reader.BookClubReaderService
import tome.exceptions.factories.ExceptionsFactory
import tome.library.LibraryBookService
import tome.library.status.LibraryBookStatusType
import java.time.LocalDate
import java.util.UUID

@Service
class BookClubBookVoteService(
    private val repo: BookClubBookVoteRepo,
    private val readerService: BookClubReaderService,
    private val bookService: BookClubBookService,
    private val libraryService: LibraryBookService,
    private val exceptionFactory: ExceptionsFactory,
) {
    @Transactional
    fun create(
        bookClubId: UUID,
        bookId: UUID,
        userId: String,
        hasAccepted: Boolean,
    ): CreateVoteResult {
        val book = bookService.read(bookClubId, bookId)
        if (book.status != BookClubBookStatus.PROPOSED) {
            throw exceptionFactory.createBadRequestException("Cannot vote on a book which is not proposed.")
        }
        val isMember = readerService.isMemberOfClub(bookClubId, userId)
        if (!isMember) {
            throw exceptionFactory.createUnauthorizedException("This user is not member of that book club.")
        }
        val vote = BookClubBookVote(id = BookClubBookVoteId(userId, bookClubId, bookId), hasAccepted = hasAccepted)
        val result = repo.save(vote)
        startReadingIfAppropriate(bookClubId, bookId)

        val updatedBook = bookService.read(bookClubId, bookId)
        val wasWinner = updatedBook.status == BookClubBookStatus.SELECTED

        return CreateVoteResult(vote = result, bookIsWinner = wasWinner)
    }

    fun checkAllMembersAccepted(
        bookClubId: UUID,
        bookId: UUID,
    ): Boolean {
        val memberCount = readerService.countBookClubMembers(bookClubId)
        val acceptedCount =
            repo.countById_BookClubIdAndId_BookIdAndHasAccepted(
                bookClubId,
                bookId,
                hasAccepted = true,
            )
        return memberCount == acceptedCount
    }

    @Transactional
    fun startReadingIfAppropriate(
        bookClubId: UUID,
        bookId: UUID,
    ) {
        if (checkAllMembersAccepted(bookClubId, bookId)) {
            bookService.updateState(bookClubId, bookId, BookClubBookStatus.SELECTED)
            val members = readerService.getBookClubMembers(bookClubId)
            for (member in members) {
                libraryService.createLibraryBook(bookId, LibraryBookStatusType.READING, 0, LocalDate.now(), member)
            }
        }
    }

    fun read(
        bookClubId: UUID,
        bookId: UUID,
        userId: String,
    ): BookClubBookVote? = repo.findById(BookClubBookVoteId(userId, bookClubId, bookId)).orElse(null)

    fun readAll(
        bookClubId: UUID,
        bookId: UUID,
    ): Map<String, List<BookClubBookVote>> {
        val members: List<String> = readerService.getBookClubMembers(bookClubId)
        val votes: List<BookClubBookVote> = repo.findById_BookClubIdAndId_BookId(bookClubId, bookId)

        val votesByUser = votes.associateBy { it.id.userId }

        val missingVotes =
            members
                .filter { it !in votesByUser.keys }
                .map { member ->
                    BookClubBookVote(
                        id =
                            BookClubBookVoteId(
                                userId = member,
                                bookClubId = bookClubId,
                                bookId = bookId,
                            ),
                        hasAccepted = null,
                    )
                }

        val allVotes = votes + missingVotes

        return allVotes.groupBy {
            when (it.hasAccepted) {
                true -> "accepted"
                false -> "rejected"
                null -> "missing"
            }
        }
    }

    @Transactional
    fun updateVote(
        bookClubId: UUID,
        bookId: UUID,
        userId: String,
        newValue: Boolean,
    ): BookClubBookVote {
        val voteId = BookClubBookVoteId(userId, bookClubId, bookId)
        val vote =
            repo.findById(voteId).orElseThrow {
                exceptionFactory.createNotFoundException(
                    "That user has not voted on that book club proposal.",
                )
            }
        vote.hasAccepted = newValue
        startReadingIfAppropriate(bookClubId, bookId)
        return vote
    }
}
