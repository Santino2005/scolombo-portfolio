package tome.books

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BookRepository : JpaRepository<Book, UUID> {
    @Query(
        """SELECT DISTINCT b 
       FROM Book b 
       LEFT JOIN b.authors a 
       LEFT JOIN b.tags t
       WHERE (LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) 
              OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) 
              OR LOWER(a.surname) LIKE LOWER(CONCAT('%', :search, '%'))
              OR LOWER(a.fullName) LIKE LOWER(CONCAT('%', :search, '%')) 
              OR b.isbn LIKE CONCAT('%', :search, '%'))
         AND (:tags IS NULL OR t.name IN :tags)
    """,
    )
    fun searchBook(
        @Param("search") search: String,
        @Param("tags") tags: List<String>?,
        pageable: Pageable,
    ): Page<Book>

    fun findBookByIsbn(isbn: String): Book?
}
