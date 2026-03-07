package tome.library

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import tome.books.Book
import tome.books.BookRepository
import tome.books.author.Author
import tome.books.author.AuthorRepository
import tome.books.languageApi.Language
import tome.books.languageApi.LanguageRepository
import tome.books.publisherApi.Publisher
import tome.books.publisherApi.PublisherRepository
import tome.books.tag.Tag
import tome.books.tag.TagRepository
import tome.library.status.LibraryBookStatusType
import java.time.LocalDate

@ActiveProfiles("test")
@TestPropertySource(
    properties = ["AUTH0_ISSUER_URI=https://dummy-issuer.com/"],
)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class LibraryBookRepositoryTest {
    companion object {
        @Container
        val postgres = PostgreSQLContainer<Nothing>("postgres:15-alpine")

        @JvmStatic
        @DynamicPropertySource
        fun registerDatasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    private val userId1 = "auth0|user-1"
    private val userId2 = "auth0|user-2"

    private lateinit var bookBorges: Book
    private lateinit var bookCortazar: Book

    @Autowired private lateinit var libraryBookRepository: LibraryBookRepository

    @Autowired private lateinit var bookRepository: BookRepository

    @Autowired private lateinit var authorRepository: AuthorRepository

    @Autowired private lateinit var languageRepository: LanguageRepository

    @Autowired private lateinit var publisherRepository: PublisherRepository

    @Autowired private lateinit var tagRepository: TagRepository

    @BeforeEach
    fun setup() {
        val language = languageRepository.save(Language(id = null, name = "Español"))
        val publisher = publisherRepository.save(Publisher(id = null, name = "Sudamericana"))
        val tag = tagRepository.save(Tag(id = null, name = "Ficción Latinoamericana"))
        val borges = authorRepository.save(Author(id = null, name = "Jorge Luis", surname = "Borges"))
        val cortazar = authorRepository.save(Author(id = null, name = "Julio", surname = "Cortázar"))

        bookBorges =
            bookRepository.save(
                Book(
                    id = null,
                    title = "Ficciones",
                    isbn = "978-8420633118",
                    releasedDate = LocalDate.now(),
                    pages = 200,
                    coverUrl = "",
                    synopsis = "",
                    language = language,
                    publisher = publisher,
                    tags = listOf(tag),
                    authors = listOf(borges),
                ),
            )

        bookCortazar =
            bookRepository.save(
                Book(
                    id = null,
                    title = "Rayuela",
                    isbn = "978-8437604085",
                    releasedDate = LocalDate.now(),
                    pages = 600,
                    coverUrl = "",
                    synopsis = "",
                    language = language,
                    publisher = publisher,
                    tags = listOf(tag),
                    authors = listOf(cortazar),
                ),
            )

        libraryBookRepository.save(LibraryBook(id = null, userId = userId1, book = bookBorges, status = LibraryBookStatusType.READING))
        libraryBookRepository.save(LibraryBook(id = null, userId = userId1, book = bookCortazar, status = LibraryBookStatusType.READ))
    }

    @Nested
    inner class `Tests para findByStatusAndUserId` {
        @Test
        fun `deberia encontrar libros por estado y usuario`() {
            val result = libraryBookRepository.findByStatusAndUserId(LibraryBookStatusType.READING, userId1)

            val foundBook = result.find { it.book.title == "Ficciones" }
            assertThat(foundBook).isNotNull
        }

        @Test
        fun `deberia devolver lista vacia si el estado no coincide`() {
            val result = libraryBookRepository.findByStatusAndUserId(LibraryBookStatusType.DNF, userId1)
            assertThat(result).isEmpty()
        }
    }

    @Nested
    inner class `Tests para findByUserIdAndBookId` {
        @Test
        fun `deberia encontrar una entrada por usuario y ID de libro`() {
            val result = libraryBookRepository.findByUserIdAndBookId(userId1, bookBorges.id!!)
            assertThat(result).isNotNull
            assertThat(result?.book?.title).isEqualTo("Ficciones")
        }

        @Test
        fun `deberia devolver null si el ID del libro no pertenece al usuario`() {
            val result = libraryBookRepository.findByUserIdAndBookId(userId2, bookCortazar.id!!)
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class `Tests para findByTitleAuthorNameOrAuthorSurnameOrIsbnPersonalBooks` {
        @Test
        fun `deberia encontrar por titulo parcial`() {
            val result =
                libraryBookRepository.searchBook(
                    userId1,
                    "Rayu",
                    null,
                    PageRequest.of(0, 10),
                )
            val foundBook = result.content.find { it.book.title.contains("Rayu") }
            assertThat(foundBook).isNotNull
        }

        @Test
        fun `deberia encontrar por apellido de autor`() {
            val result =
                libraryBookRepository.searchBook(
                    userId1,
                    "Borges",
                    null,
                    PageRequest.of(0, 10),
                )
            val foundAuthor = result.content.flatMap { it.book.authors }.find { it.surname == "Borges" }
            assertThat(foundAuthor).isNotNull
        }

        @Test
        fun `deberia encontrar por ISBN`() {
            val result =
                libraryBookRepository.searchBook(
                    userId1,
                    "978-8437604085",
                    null,
                    PageRequest.of(0, 10),
                )
            val foundBook = result.content.find { it.book.isbn == "978-8437604085" }
            assertThat(foundBook).isNotNull
        }

        @Test
        fun `no deberia encontrar libros de otros usuarios`() {
            val result =
                libraryBookRepository.searchBook(
                    userId2,
                    "Rayuela",
                    null,
                    PageRequest.of(0, 10),
                )
            assertThat(result.content).isEmpty()
        }
    }
}
