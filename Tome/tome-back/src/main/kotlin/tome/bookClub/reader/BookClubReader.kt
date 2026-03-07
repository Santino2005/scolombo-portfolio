package tome.bookClub.reader

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.NotBlank
import tome.bookClub.BookClub
import java.util.UUID

@Entity
class BookClubReader(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    @ManyToOne
    val bookClub: BookClub,
    @NotBlank
    val userId: String,
    val deleted: Boolean,
)
