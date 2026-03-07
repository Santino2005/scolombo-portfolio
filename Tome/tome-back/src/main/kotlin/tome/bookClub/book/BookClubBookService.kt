package tome.bookClub.book

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tome.bookClub.BookClubService
import tome.bookClub.book.vote.BookClubBookVoteRepo
import tome.bookClub.reader.BookClubReaderService
import tome.books.BookService
import tome.exceptions.factories.ExceptionsFactory
import tome.library.LibraryBookService
import java.util.UUID

@Service
class BookClubBookService(
    private val repo: BookClubBookRepo,
    private val bookClubReaderService: BookClubReaderService,
    private val bookClubService: BookClubService,
    private val bookService: BookService,
    private val libraryService: LibraryBookService,
    private val exceptionsFactory: ExceptionsFactory,
    private val voteRepo: BookClubBookVoteRepo,
) {
    @Transactional
    fun create(
        userId: String,
        bookClubId: UUID,
        bookId: UUID,
    ): BookClubBook {
        val bookClub = bookClubService.getBookClubById(bookClubId)
        if (!bookClubReaderService.isMemberOfClub(bookClub, userId)) {
            throw exceptionsFactory.createUnauthorizedException(
                "You're not allowed to propose books to that book club.",
            )
        }

        val book =
            bookService.findById(bookId)
                ?: throw exceptionsFactory.createNotFoundException("That book does not exist.")

        if (!libraryService.existsBookInUserLibrary(userId, book)) {
            throw exceptionsFactory.createNotFoundException("Book is not part of user library.")
        }
        val bookClubBookId = BookClubBookId(bookClubId = bookClubId, bookId = bookId)
        val bookClubBook = repo.findByBookClub_IdAndBook_Id(bookClubId, bookId)
        if (bookClubBook != null) {
            if (bookClubBook.status != BookClubBookStatus.FINISHED && bookClubBook.status != BookClubBookStatus.REJECTED) {
                throw exceptionsFactory.createConflictException("Cannot re-propose a book which is not finished or rejected")
            }
            return this.updateState(bookClubId, bookId, BookClubBookStatus.PROPOSED)
        }
        return repo.save(BookClubBook(id = bookClubBookId, bookClub = bookClub, book = book))
    }

    fun read(
        bookClubId: UUID,
        bookId: UUID,
    ): BookClubBook =
        repo.findByBookClub_IdAndBook_Id(bookClubId, bookId)
            ?: throw exceptionsFactory.createNotFoundException(
                "That club does not have that book",
            )

    fun readAll(
        status: BookClubBookStatus,
        bookClubId: UUID,
        pageable: Pageable,
    ): Page<BookClubBook> = repo.findByBookClub_IdAndStatus(bookClubId, status, pageable)

    @Transactional
    fun updateState(
        bookClubId: UUID,
        bookId: UUID,
        newStatus: BookClubBookStatus,
    ): BookClubBook {
        val book =
            repo.findByBookClub_IdAndBook_Id(bookClubId, bookId)
                ?: throw exceptionsFactory.createNotFoundException("That book club book does not exist.")
        if (newStatus == BookClubBookStatus.FINISHED) {
            val votes = voteRepo.findById_BookClubIdAndId_BookId(bookClubId, bookId)
            if (votes.isNotEmpty()) {
                voteRepo.deleteAll(votes)
            }
        }
        book.status = newStatus
        return repo.save(book)
    }

    @Transactional
    fun readCurrent(
        bookClubId: UUID,
        userId: String,
    ): BookClubBook? {
        val bookClub = bookClubService.getBookClubById(bookClubId)
        if (!bookClubReaderService.isMemberOfClub(bookClub, userId)) {
            throw exceptionsFactory.createNotFoundException("You are not a member of this book club")
        }
        return repo.findFirstByBookClub_IdAndStatus(bookClubId, BookClubBookStatus.SELECTED)
    }
}
