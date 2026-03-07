package tome.books

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import tome.books.author.Author
import tome.books.languageApi.Language
import tome.books.publisherApi.Publisher
import tome.books.tag.Tag
import java.time.LocalDate
import java.util.UUID

@Entity
class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = UUID.randomUUID(),
    val title: String,
    val isbn: String,
    val releasedDate: LocalDate,
    val pages: Int,
    val coverUrl: String,
    @Column(columnDefinition = "TEXT", length = 5000)
    val synopsis: String,
    @ManyToOne
    val language: Language,
    @ManyToOne
    val publisher: Publisher,
    @ManyToMany
    val tags: List<Tag>,
    @ManyToMany
    val authors: List<Author>,
)
