package tome.books.tag

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = UUID.randomUUID(),
    val name: String,
)
