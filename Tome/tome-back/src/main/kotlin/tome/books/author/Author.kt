package tome.books.author

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = UUID.randomUUID(),
    val name: String,
    val surname: String,
    val fullName: String = "",
)
