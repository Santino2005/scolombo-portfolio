package tome.books.languageApi

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LanguageRepository : JpaRepository<Language, UUID> {
    fun findByName(name: String): Language?
}
