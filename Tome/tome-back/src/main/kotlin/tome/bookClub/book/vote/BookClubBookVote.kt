package tome.bookClub.book.vote

import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import java.io.Serializable
import java.util.UUID

@Embeddable
data class BookClubBookVoteId(
    val userId: String,
    val bookClubId: UUID,
    val bookId: UUID,
) : Serializable

@Entity
class BookClubBookVote(
    @EmbeddedId
    val id: BookClubBookVoteId,
    var hasAccepted: Boolean? = null,
)
