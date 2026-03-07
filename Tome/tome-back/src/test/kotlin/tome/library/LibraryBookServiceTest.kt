package tome.library

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
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
import tome.exceptions.BasicHttpException
import tome.library.status.LibraryBookStatusType
import java.time.LocalDate
import java.util.UUID

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource(
    properties = ["AUTH0_ISSUER_URI=https://dummy-issuer.com/"],
)
@Transactional
class LibraryBookServiceTest {
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

    @Autowired private lateinit var libraryBookService: LibraryBookService

    @Autowired private lateinit var libraryBookRepository: LibraryBookRepository

    @Autowired private lateinit var bookRepository: BookRepository

    @Autowired private lateinit var authorRepository: AuthorRepository

    @Autowired private lateinit var publisherRepository: PublisherRepository

    @Autowired private lateinit var languageRepository: LanguageRepository

    private lateinit var bookBorges: Book
    private lateinit var bookCortazar: Book
    private val userId = "auth0|user-123"

    @BeforeEach
    fun setup() {
        libraryBookRepository.deleteAll()
        bookRepository.deleteAll()
        authorRepository.deleteAll()
        publisherRepository.deleteAll()
        languageRepository.deleteAll()

        val language = languageRepository.save(Language(id = null, name = "Español"))
        val publisher = publisherRepository.save(Publisher(id = null, name = "Sudamericana"))
        val borges = authorRepository.save(Author(id = null, name = "Jorge Luis", surname = "Borges"))
        val cortazar = authorRepository.save(Author(id = null, name = "Julio", surname = "Cortázar"))

        bookBorges =
            bookRepository.save(
                Book(
                    id = null,
                    title = "El Aleph",
                    isbn = "111",
                    releasedDate = LocalDate.now(),
                    pages = 200,
                    coverUrl = "",
                    synopsis = "",
                    language = language,
                    publisher = publisher,
                    tags = emptyList(),
                    authors = listOf(borges),
                ),
            )
        bookCortazar =
            bookRepository.save(
                Book(
                    id = null,
                    title = "Rayuela",
                    isbn = "222",
                    releasedDate = LocalDate.now(),
                    pages = 600,
                    coverUrl = "",
                    synopsis = "",
                    language = language,
                    publisher = publisher,
                    tags = emptyList(),
                    authors = listOf(cortazar),
                ),
            )
    }

    @Test
    fun `createLibraryBook deberia crear una nueva entrada si no existe`() {
        val result = libraryBookService.createLibraryBook(bookBorges.id!!, LibraryBookStatusType.WANT_TO_READ, 0, null, userId)
        val found = libraryBookRepository.findByUserIdAndBookId(userId, bookBorges.id!!)
        assertNotNull(found)
        assertEquals(LibraryBookStatusType.WANT_TO_READ, found?.status)
        assertEquals(result.id, found?.id)
    }

    @Test
    fun `createLibraryBook deberia actualizar una entrada existente`() {
        libraryBookService.createLibraryBook(bookBorges.id!!, LibraryBookStatusType.READING, 50, LocalDate.now(), userId)
        val updatedEntry = libraryBookRepository.findByUserIdAndBookId(userId, bookBorges.id!!)
        assertNotNull(updatedEntry)
        assertEquals(LibraryBookStatusType.READING, updatedEntry?.status)
        assertEquals(50, updatedEntry?.currentPage)
    }

    @Test
    fun `createLibraryBook deberia lanzar NotFoundException si el libro no existe`() {
        val nonExistentBookId = UUID.randomUUID()
        assertThrows<BasicHttpException> {
            libraryBookService.createLibraryBook(nonExistentBookId, LibraryBookStatusType.WANT_TO_READ, 0, null, userId)
        }
    }

    @Test
    fun `getLibraryBookByStatus deberia devolver solo libros con estado READING`() {
        libraryBookRepository.save(LibraryBook(id = null, userId = userId, status = LibraryBookStatusType.READING, book = bookBorges))
        libraryBookRepository.save(LibraryBook(id = null, userId = userId, status = LibraryBookStatusType.READ, book = bookCortazar))

        val result = libraryBookService.getLibraryBookByStatus(LibraryBookStatusType.READING, userId)

        assertEquals(1, result.size)
        assertEquals("El Aleph", result[0].book.title)
    }

    @Test
    fun `getLibraryBookByStatus deberia devolver lista vacia cuando no hay libros con el estado especificado`() {
        libraryBookRepository.save(LibraryBook(id = null, userId = userId, status = LibraryBookStatusType.READING, book = bookBorges))

        val result = libraryBookService.getLibraryBookByStatus(LibraryBookStatusType.WANT_TO_READ, userId)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getLibraryBook deberia devolver la entrada si existe`() {
        val entry =
            libraryBookRepository.save(
                LibraryBook(id = null, userId = userId, status = LibraryBookStatusType.READING, book = bookBorges),
            )

        val foundEntry = libraryBookService.getLibraryBook(userId, bookBorges.id!!)
        val notFoundEntry = libraryBookService.getLibraryBook(userId, UUID.randomUUID())

        assertNotNull(foundEntry)
        assertEquals(entry.id, foundEntry?.id)
        assertNull(notFoundEntry)
    }

    @Test
    fun `deleteLibraryBook deberia eliminar la entrada`() {
        libraryBookService.createLibraryBook(bookBorges.id!!, LibraryBookStatusType.READING, 50, LocalDate.now(), userId)
        libraryBookRepository.flush()
        val entryBeforeDelete = libraryBookRepository.findByUserIdAndBookId(userId, bookBorges.id!!)
        assertNotNull(entryBeforeDelete)
        libraryBookService.deleteLibraryBook(userId, bookBorges.id!!)
        val entryAfterDelete = libraryBookRepository.findByUserIdAndBookId(userId, bookBorges.id!!)
        assertNull(entryAfterDelete)
    }

    @Test
    fun `findByLibraryBookTitleAuthorNameOrAuthorSurnameOrIsbn deberia encontrar por apellido de autor`() {
        libraryBookRepository.save(LibraryBook(id = null, userId = userId, status = LibraryBookStatusType.READ, book = bookBorges))
        libraryBookRepository.save(
            LibraryBook(id = null, userId = "another-user", status = LibraryBookStatusType.READ, book = bookCortazar),
        )

        val pageable = PageRequest.of(0, 10)
        val result = libraryBookService.findByLibraryBookTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(userId, "Borges", null, pageable)
        val emptyResult = libraryBookService.findByLibraryBookTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(userId, "Cortazar", null, pageable)

        assertEquals(1, result.totalElements)

        val foundBook = result.find { it.book.id == bookBorges.id }
        assertEquals(bookBorges.title, foundBook?.book?.title)
        assertTrue(emptyResult.isEmpty)
    }

    @Test
    fun `changeStatus deberia cambiar correctamente de WANT_TO_READ a READING`() {
        val libraryBook =
            libraryBookRepository.save(
                LibraryBook(id = null, userId = userId, status = LibraryBookStatusType.WANT_TO_READ, book = bookBorges),
            )

        libraryBookService.changeStatus(
            newStatus = LibraryBookStatusType.READING,
            currentPage = 25,
            libraryBook = libraryBook,
            startedAt = LocalDate.now(),
        )

        assertEquals(LibraryBookStatusType.READING, libraryBook.status)
        assertEquals(25, libraryBook.currentPage)
        assertNotNull(libraryBook.startedAt)
        assertNull(libraryBook.finishedAt)
    }

    @Test
    fun `changeStatus deberia cambiar correctamente de READING a READ`() {
        val libraryBook =
            libraryBookRepository.save(
                LibraryBook(
                    id = null,
                    userId = userId,
                    status = LibraryBookStatusType.READING,
                    book = bookBorges,
                    currentPage = 100,
                    startedAt = LocalDate.now().minusDays(5),
                ),
            )

        libraryBookService.changeStatus(
            newStatus = LibraryBookStatusType.READ,
            currentPage = 200,
            libraryBook = libraryBook,
            finishedAt = LocalDate.now(),
        )

        assertEquals(LibraryBookStatusType.READ, libraryBook.status)
        assertEquals(bookBorges.pages, libraryBook.currentPage)
        assertNotNull(libraryBook.startedAt)
        assertNotNull(libraryBook.finishedAt)
    }

    @Test
    fun `changeStatus deberia cambiar correctamente a DNF`() {
        val libraryBook =
            libraryBookRepository.save(
                LibraryBook(
                    id = null,
                    userId = userId,
                    status = LibraryBookStatusType.READING,
                    book = bookBorges,
                    currentPage = 50,
                ),
            )

        libraryBookService.changeStatus(
            newStatus = LibraryBookStatusType.DNF,
            currentPage = 50,
            libraryBook = libraryBook,
            startedAt = LocalDate.now().minusDays(3),
        )

        assertEquals(LibraryBookStatusType.DNF, libraryBook.status)
        assertNotNull(libraryBook.startedAt)
        assertNull(libraryBook.finishedAt)
    }
}
