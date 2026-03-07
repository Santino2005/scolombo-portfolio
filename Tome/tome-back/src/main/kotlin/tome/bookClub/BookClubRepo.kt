package tome.bookClub

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BookClubRepo : JpaRepository<BookClub, UUID> {
    fun searchById(id: UUID): BookClub?
}
