package tome.library

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import tome.books.Book
import tome.library.status.LibraryBookStatusType
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "book_id"])],
)
class LibraryBook(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = UUID.randomUUID(),
    val userId: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    var status: LibraryBookStatusType,
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    val book: Book,
    var currentPage: Int = 0,
    var startedAt: LocalDate? = null,
    var finishedAt: LocalDate? = null,
)
