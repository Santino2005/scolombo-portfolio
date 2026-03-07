package tome.books.tag

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TagRepository : JpaRepository<Tag, UUID> {
    @Query(
        """SELECT DISTINCT t 
       FROM Tag t
       WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%'))
       """,
    )
    fun searchByName(search: String): List<Tag>

    fun findByName(name: String): Tag?
}
