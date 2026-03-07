package tome.books.author

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AuthorRepository : JpaRepository<Author, UUID> {
    fun findByFullName(fullName: String): Author?
}
