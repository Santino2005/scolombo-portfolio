package tome.bookClub.book

import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import tome.bookClub.BookClub
import tome.books.Book
import java.io.Serializable
import java.util.UUID

@Embeddable
data class BookClubBookId(
    val bookClubId: UUID? = null,
    val bookId: UUID? = null,
) : Serializable

@Entity
class BookClubBook(
    @EmbeddedId
    val id: BookClubBookId,
    @MapsId("bookClubId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_club_id")
    val bookClub: BookClub,
    @MapsId("bookId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    val book: Book,
    @Enumerated(EnumType.STRING)
    var status: BookClubBookStatus = BookClubBookStatus.PROPOSED,
)
