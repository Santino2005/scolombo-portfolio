package tome.books.publisherApi

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PublisherRepository : JpaRepository<Publisher, UUID> {
    fun findByName(name: String): Publisher?
}
