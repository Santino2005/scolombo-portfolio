package tome.library

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import tome.books.Book
import tome.books.BookRepository
import tome.books.author.Author
import tome.books.author.AuthorRepository
import tome.books.languageApi.Language
import tome.books.languageApi.LanguageRepository
import tome.books.publisherApi.Publisher
import tome.books.publisherApi.PublisherRepository
import tome.library.dto.LibraryBookStatusTypeDTO
import tome.library.status.LibraryBookStatusType
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(
    properties = ["AUTH0_ISSUER_URI=https://jorge-dummy-issuer.com/"],
)
class LibraryBookControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired private lateinit var libraryBookRepository: LibraryBookRepository

    @Autowired private lateinit var bookRepository: BookRepository

    @Autowired private lateinit var authorRepository: AuthorRepository

    @Autowired private lateinit var publisherRepository: PublisherRepository

    @Autowired private lateinit var languageRepository: LanguageRepository

    private lateinit var bookBorges: Book
    private lateinit var bookCortazar: Book
    private val userId = "auth0|user-123"

    companion object {
        @Container
        val postgres =
            PostgreSQLContainer<Nothing>("postgres:15-alpine").apply {
                start()
            }

        @JvmStatic
        @DynamicPropertySource
        fun registerDatasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

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
    fun `POST libraryBook deberia crear una entrada y devolver 200 OK`() {
        val bookId = bookBorges.id!!
        val requestDTO =
            LibraryBookStatusTypeDTO(
                readingStatus = LibraryBookStatusType.READING,
                currentPage = 50,
                startedAt = "2025-10-10",
                finishedAt = null,
            )

        mockMvc
            .perform(
                post("/libraries/books/{bookId}", bookId)
                    .with(jwt().jwt { it.subject(userId) })
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO)),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.readingStatus", `is`("READING")))
            .andExpect(jsonPath("$.currentPage", `is`(50)))
    }

    @Test
    fun `GET filterLibraryBookByStatus deberia devolver una lista de libros filtrada`() {
        libraryBookRepository.save(LibraryBook(id = null, userId = userId, status = LibraryBookStatusType.READ, book = bookBorges))
        libraryBookRepository.save(
            LibraryBook(id = null, userId = userId, status = LibraryBookStatusType.WANT_TO_READ, book = bookCortazar),
        )

        mockMvc
            .perform(
                get("/libraries")
                    .param("filter", "READ")
                    .with(jwt().jwt { it.subject(userId) }),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$[?(@.book.title == 'El Aleph')]", hasSize<Any>(1)))
    }

    @Test
    fun `DELETE deleteLibraryBook deberia eliminar la entrada y devolver 204 No Content`() {
        libraryBookRepository.save(LibraryBook(id = null, userId = userId, status = LibraryBookStatusType.READ, book = bookBorges))

        mockMvc
            .perform(
                delete("/libraries/books/{bookId}", bookBorges.id!!)
                    .with(jwt().jwt { it.subject(userId) }),
            ).andExpect(status().isNoContent)
    }

    @Test
    fun `GET getPersonalLibrary con search deberia devolver libros paginados del usuario`() {
        libraryBookRepository.save(LibraryBook(id = null, userId = userId, status = LibraryBookStatusType.READ, book = bookBorges))
        libraryBookRepository.save(
            LibraryBook(id = null, userId = userId, status = LibraryBookStatusType.WANT_TO_READ, book = bookCortazar),
        )
        libraryBookRepository.save(LibraryBook(id = null, userId = "another-user", status = LibraryBookStatusType.READ, book = bookBorges))

        mockMvc
            .perform(
                get("/libraries/personal")
                    .param("search", "Borges")
                    .with(jwt().jwt { it.subject(userId) }),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.content[?(@.book.title == 'El Aleph')]", hasSize<Any>(1)))
            .andExpect(jsonPath("$.totalElements", `is`(1)))
    }

    @Test
    fun `GET getPersonalLibrary con search en blanco deberia devolver 400 Bad Request`() {
        mockMvc
            .perform(
                get("/libraries/personal")
                    .param("search", "  ")
                    .with(jwt().jwt { it.subject(userId) }),
            ).andExpect(status().isBadRequest)
    }

    @Test
    fun `GET getPersonalLibrary sin search deberia devolver libros agrupados por estado`() {
        libraryBookRepository.save(LibraryBook(id = null, userId = userId, status = LibraryBookStatusType.READING, book = bookBorges))
        libraryBookRepository.save(LibraryBook(id = null, userId = userId, status = LibraryBookStatusType.READ, book = bookCortazar))
        libraryBookRepository.save(
            LibraryBook(id = null, userId = "another-user", status = LibraryBookStatusType.READING, book = bookBorges),
        )

        mockMvc
            .perform(
                get("/libraries/personal")
                    .with(jwt().jwt { it.subject(userId) }),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.READING[?(@.book.title == 'El Aleph')]", hasSize<Any>(1)))
            .andExpect(jsonPath("$.READ[?(@.book.title == 'Rayuela')]", hasSize<Any>(1)))
            .andExpect(jsonPath("$.WANT_TO_READ", hasSize<Any>(0)))
            .andExpect(jsonPath("$.DNF", hasSize<Any>(0)))
    }
}
