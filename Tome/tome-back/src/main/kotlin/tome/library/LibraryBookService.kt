package tome.library

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import tome.Auth0ManagementService
import tome.UserProfileDTO
import tome.books.Book
import tome.books.BookRepository
import tome.exceptions.factories.ExceptionsFactory
import tome.library.status.LibraryBookStatusType
import java.time.LocalDate
import java.util.UUID

@Service
class LibraryBookService(
    val libraryBookRepository: LibraryBookRepository,
    val bookRepository: BookRepository,
    val auth0Service: Auth0ManagementService,
) {
    fun createLibraryBook(
        bookId: UUID,
        libraryBookStatusType: LibraryBookStatusType,
        currentPage: Int?,
        startedAt: LocalDate?,
        userId: String,
        finishedAt: LocalDate? = null,
    ): LibraryBook {
        val book =
            bookRepository.findById(bookId).orElseThrow {
                ExceptionsFactory().createNotFoundException("Book with id: $bookId not found")
            }

        val libraryBook =
            libraryBookRepository.findByUserIdAndBookId(userId, bookId)
                ?: return libraryBookRepository.save(
                    LibraryBook(
                        id = null,
                        userId = userId,
                        status = libraryBookStatusType,
                        book = book,
                        currentPage = currentPage ?: 0,
                        startedAt = startedAt,
                    ),
                )

        if (currentPage != null && (currentPage > book.pages || currentPage < 0)) {
            throw ExceptionsFactory()
                .createBadRequestException("Pages must be between 0 and ${book.pages}")
        }

        changeStatus(
            newStatus = libraryBookStatusType,
            currentPage = currentPage ?: libraryBook.currentPage,
            libraryBook = libraryBook,
            startedAt = startedAt,
            finishedAt = finishedAt,
        )

        return libraryBookRepository.save(libraryBook)
    }
    fun changeStatus(
        newStatus: LibraryBookStatusType,
        currentPage: Int,
        libraryBook: LibraryBook,
        startedAt: LocalDate? = null,
        finishedAt: LocalDate? = null,
    ) {
        when (newStatus) {
            LibraryBookStatusType.New_Status -> toNewStatus(libraryBook)
            LibraryBookStatusType.WANT_TO_READ -> toWantToReadStatus(libraryBook)
            LibraryBookStatusType.READING -> toReadingStatus(libraryBook, currentPage, startedAt)
            LibraryBookStatusType.READ -> toReadStatus(libraryBook, startedAt, finishedAt)
            LibraryBookStatusType.DNF -> toDNFStatus(libraryBook, startedAt)
        }
    }
    private fun toNewStatus(libraryBook: LibraryBook) {
        libraryBook.status = LibraryBookStatusType.New_Status
        libraryBook.currentPage = 0
        libraryBook.startedAt = null
        libraryBook.finishedAt = null
    }

    fun getLibraryBookByStatus(
        status: LibraryBookStatusType,
        userId: String,
    ): List<LibraryBook> = libraryBookRepository.findByStatusAndUserId(status, userId)

    fun getLibraryBook(
        userId: String,
        bookId: UUID,
    ): LibraryBook? = libraryBookRepository.findByUserIdAndBookId(userId, bookId)



    private fun toReadStatus(
        libraryBook: LibraryBook,
        startedAt: LocalDate?,
        finishedAt: LocalDate?,
    ) {
        libraryBook.status = LibraryBookStatusType.READ
        libraryBook.currentPage = libraryBook.book.pages
        libraryBook.startedAt = startedAt ?: libraryBook.startedAt
        libraryBook.finishedAt = finishedAt
    }



    private fun toWantToReadStatus(libraryBook: LibraryBook) {
        libraryBook.status = LibraryBookStatusType.WANT_TO_READ
        libraryBook.currentPage = 0
        libraryBook.startedAt = null
        libraryBook.finishedAt = null
    }

    private fun toReadingStatus(
        libraryBook: LibraryBook,
        currentPage: Int,
        startedAt: LocalDate?,
    ) {
        libraryBook.status = LibraryBookStatusType.READING
        libraryBook.currentPage = currentPage
        if (libraryBook.startedAt == null && startedAt != null) {
            libraryBook.startedAt = startedAt
        }
        libraryBook.finishedAt = null
    }

    fun deleteLibraryBook(
        userId: String,
        bookId: UUID,
    ) {
        val libraryBook =
            libraryBookRepository.findByUserIdAndBookId(userId, bookId)
                ?: throw ExceptionsFactory()
                    .createNotFoundException(
                        "Book with id $bookId not found in library for user $userId",
                    )

        libraryBookRepository.delete(libraryBook)
    }

    private fun toDNFStatus(
        libraryBook: LibraryBook,
        startedAt: LocalDate?,
    ) {
        libraryBook.status = LibraryBookStatusType.DNF
        if (startedAt != null) {
            libraryBook.startedAt = startedAt
        }
        libraryBook.finishedAt = null
    }

    fun existsBookInUserLibrary(
        userId: String,
        book: Book,
    ): Boolean = libraryBookRepository.existsByUserIdAndBook(userId, book)

    fun findByLibraryBookTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(
        jwt: String,
        search: String,
        tags: List<String>?,
        pageable: Pageable,
    ): Page<LibraryBook> =
        libraryBookRepository.searchBook(
            jwt,
            search,
            tags,
            pageable,
        )

    fun getMembersReadingStatusByBook(
        bookId: UUID,
        memberIds: List<String>,
    ): Map<LibraryBookStatusType, List<UserProfileDTO>> {
        val statusMap = mutableMapOf<LibraryBookStatusType, MutableList<UserProfileDTO>>()

        for (memberId in memberIds) {
            val libraryBook = libraryBookRepository.findByUserIdAndBookId(memberId, bookId)
            if (libraryBook != null) {
                val userResponse = auth0Service.getUserById(memberId)
                val user = userResponse.body
                if (user != null) {
                    statusMap.computeIfAbsent(libraryBook.status) { mutableListOf() }.add(user)
                }
            }
        }

        return statusMap
    }
}
