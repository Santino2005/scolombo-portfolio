package tome.library

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import tome.books.Book
import tome.library.status.LibraryBookStatusType
import java.util.UUID

@Repository
interface LibraryBookRepository : JpaRepository<LibraryBook, String> {
    fun findByStatusAndUserId(
        status: LibraryBookStatusType,
        userId: String,
    ): List<LibraryBook>

    fun existsByUserIdAndBook(
        userId: String,
        book: Book,
    ): Boolean

    fun findByUserIdAndBookId(
        userId: String,
        bookId: UUID,
    ): LibraryBook?

    @Query(
        """
    SELECT DISTINCT lb
    FROM LibraryBook lb
    JOIN lb.book b
    LEFT JOIN b.authors a
    LEFT JOIN b.tags t
    WHERE lb.userId = :userId
      AND (
        LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) 
        OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) 
        OR LOWER(a.surname) LIKE LOWER(CONCAT('%', :search, '%'))
        OR b.isbn LIKE CONCAT('%', :search, '%')
        OR LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%'))
      )
      AND (:tags IS NULL OR t.name IN :tags)
    """,
    )
    fun searchBook(
        @Param("userId") userId: String,
        @Param("search") search: String,
        @Param("tags") tags: List<String>?,
        pageable: Pageable,
    ): Page<LibraryBook>
}
