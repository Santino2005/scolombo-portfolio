package tome.bookTesting

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
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
import java.time.LocalDate
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class BookIntegrationTest {
    companion object {
        @Container
        val postgres =
            PostgreSQLContainer<Nothing>("postgres:15-alpine").apply {
                withDatabaseName("testdb")
                withUsername("test")
                withPassword("test")
            }

        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    @LocalServerPort
    private var port: Int = 0

    @Autowired lateinit var restTemplate: TestRestTemplate

    @Autowired lateinit var bookRepository: BookRepository

    @Autowired lateinit var authorRepository: AuthorRepository

    @Autowired lateinit var tagRepository: TagRepository

    @Autowired lateinit var publisherRepository: PublisherRepository

    @Autowired lateinit var languageRepository: LanguageRepository

    @MockitoBean lateinit var jwtDecoder: JwtDecoder

    private lateinit var english: Language
    private lateinit var publisher: Publisher
    private lateinit var tag: Tag
    private lateinit var author: Author
    private lateinit var book: Book

    private fun jwtForUser(userId: String) =
        Jwt
            .withTokenValue("fake-token")
            .header("alg", "RS256")
            .claim("sub", userId)
            .build()

    @BeforeEach
    fun setup() {
        bookRepository.deleteAll()
        authorRepository.deleteAll()
        tagRepository.deleteAll()
        publisherRepository.deleteAll()
        languageRepository.deleteAll()

        english = languageRepository.save(Language(null, "English"))
        publisher = publisherRepository.save(Publisher(null, "Penguin Books"))
        tag = tagRepository.save(Tag(null, "Fiction"))
        author = authorRepository.save(Author(null, "George", "Orwell"))

        book =
            bookRepository.save(
                Book(
                    id = null,
                    title = "1984",
                    isbn = "9780451524935",
                    releasedDate = LocalDate.of(1949, 6, 8),
                    pages = 328,
                    coverUrl = "https://covers.openlibrary.org/1984.jpg",
                    synopsis = "A dystopian novel.",
                    language = english,
                    publisher = publisher,
                    tags = listOf(tag),
                    authors = listOf(author),
                ),
            )
    }

    private fun baseUrl(path: String) = "http://localhost:$port$path"

    @Test
    fun `GET book by ID - returns 200 and correct data`() {
        val jwt = jwtForUser("auth0|user123")
        whenever(jwtDecoder.decode("fake-token")).thenReturn(jwt)

        val headers = HttpHeaders()
        headers["Authorization"] = "Bearer fake-token"

        val entity = HttpEntity<Unit>(headers)
        val response =
            restTemplate.exchange(
                baseUrl("/books/${book.id}"),
                HttpMethod.GET,
                entity,
                Map::class.java,
            )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(book.title, response.body?.get("title"))
        assertEquals(book.isbn, response.body?.get("isbn"))
    }

    @Test
    fun `GET book by ID - returns 404 when not found`() {
        val jwt = jwtForUser("auth0|user123")
        whenever(jwtDecoder.decode("fake-token")).thenReturn(jwt)

        val headers = HttpHeaders()
        headers["Authorization"] = "Bearer fake-token"

        val entity = HttpEntity<Unit>(headers)
        val response =
            restTemplate.exchange(
                baseUrl("/books/${UUID.randomUUID()}"),
                HttpMethod.GET,
                entity,
                String::class.java,
            )

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `GET search books by partial title`() {
        val jwt = jwtForUser("auth0|user123")
        whenever(jwtDecoder.decode("fake-token")).thenReturn(jwt)

        val headers = HttpHeaders()
        headers["Authorization"] = "Bearer fake-token"

        val entity = HttpEntity<Unit>(headers)
        val response =
            restTemplate.exchange(
                baseUrl("/books?search=198&page=0&size=10"),
                HttpMethod.GET,
                entity,
                Map::class.java,
            )

        assertEquals(HttpStatus.OK, response.statusCode)
        val content = response.body?.get("content") as List<*>
        assertNotNull(content)
        assertEquals(1, content.size)
        val bookFound = content.first() as Map<*, *>
        assertEquals("1984", bookFound["title"])
    }

    @Test
    fun `GET books unauthorized without JWT`() {
        val response = restTemplate.getForEntity(baseUrl("/books/${book.id}"), String::class.java)
        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }
}
