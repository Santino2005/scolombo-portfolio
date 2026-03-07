package tome.bookTesting

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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
import tome.books.tag.Tag
import tome.books.tag.TagRepository
import java.time.LocalDate
import kotlin.test.assertEquals

@ActiveProfiles("test")
@TestPropertySource(
    properties = [
        "AUTH0_ISSUER_URI=https://my-accountcom/",
    ],
)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class BookRepositoryTest {
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
        fun registerDatasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    @Autowired private lateinit var bookRepository: BookRepository

    @Autowired private lateinit var authorRepository: AuthorRepository

    @Autowired private lateinit var tagRepository: TagRepository

    @Autowired private lateinit var publisherRepository: PublisherRepository

    @Autowired private lateinit var languageRepository: LanguageRepository

    private lateinit var english: Language
    private lateinit var spanish: Language
    private lateinit var publisher: Publisher
    private lateinit var tags: List<Tag>
    private lateinit var tolkien: Author
    private lateinit var austen: Author
    private lateinit var cervantes: Author

    private lateinit var book1: Book
    private lateinit var book2: Book
    private lateinit var book3: Book
    private lateinit var book4: Book

    @BeforeEach
    @Transactional
    fun setup() {
        english = languageRepository.save(Language(id = null, name = "English"))
        spanish = languageRepository.save(Language(id = null, name = "Spanish"))
        publisher = publisherRepository.save(Publisher(id = null, name = "Penguin Classics"))
        tags = tagRepository.saveAll(listOf(Tag(id = null, name = "Novel"), Tag(id = null, name = "Classic")))

        tolkien = authorRepository.save(Author(id = null, name = "J.R.R.", surname = "Tolkien"))
        austen = authorRepository.save(Author(id = null, name = "Jane", surname = "Austen"))
        cervantes = authorRepository.save(Author(id = null, name = "Miguel", surname = "de Cervantes"))

        book1 =
            bookRepository.save(
                Book(
                    id = null,
                    title = "The Lord of the Rings",
                    isbn = "9780261102385",
                    releasedDate = LocalDate.of(1954, 7, 29),
                    pages = 1216,
                    coverUrl = "https://covers.openlibrary.org/lotr.jpg",
                    synopsis = "An epic high fantasy novel.",
                    language = english,
                    publisher = publisher,
                    tags = tags,
                    authors = listOf(tolkien),
                ),
            )
        book2 =
            bookRepository.save(
                Book(
                    id = null,
                    title = "Pride and Prejudice",
                    isbn = "9780141439518",
                    releasedDate = LocalDate.of(1813, 1, 28),
                    pages = 432,
                    coverUrl = "https://covers.openlibrary.org/pride.jpg",
                    synopsis = "A romantic novel by Jane Austen.",
                    language = english,
                    publisher = publisher,
                    tags = tags,
                    authors = listOf(austen),
                ),
            )
        book3 =
            bookRepository.save(
                Book(
                    id = null,
                    title = "Don Quijote de la Mancha",
                    isbn = "9788491050294",
                    releasedDate = LocalDate.of(1605, 1, 16),
                    pages = 863,
                    coverUrl = "https://covers.openlibrary.org/quijote.jpg",
                    synopsis = "A Spanish novel by Miguel de Cervantes.",
                    language = spanish,
                    publisher = publisher,
                    tags = tags,
                    authors = listOf(cervantes),
                ),
            )
        book4 =
            bookRepository.save(
                Book(
                    id = null,
                    title = "The Hobbit",
                    isbn = "9780261102217",
                    releasedDate = LocalDate.of(1937, 9, 21),
                    pages = 310,
                    coverUrl = "https://covers.openlibrary.org/hobbit.jpg",
                    synopsis = "A fantasy novel by J.R.R. Tolkien.",
                    language = english,
                    publisher = publisher,
                    tags = tags,
                    authors = listOf(tolkien),
                ),
            )
    }

    @Test
    fun `searchBook finds by exact title`() {
        val result =
            bookRepository.searchBook(
                book1.title,
                null,
                PageRequest.of(0, 10, Sort.by("title")),
            )

        assertEquals(1, result.totalElements)
        val foundBook = result.content.find { it.title == book1.title }
        assertNotNull(foundBook)
    }

    @Test
    fun `searchBook finds by partial title`() {
        val searchTerm = book2.title.substring(0, 5)
        val result = bookRepository.searchBook(searchTerm, null, PageRequest.of(0, 10))

        val foundBook = result.content.find { it.title.contains(searchTerm) }
        assertNotNull(foundBook)
    }

    @Test
    fun `searchBook finds by exact author surname`() {
        val result = bookRepository.searchBook(cervantes.surname, null, PageRequest.of(0, 10))

        assertEquals(1, result.totalElements)
        val foundAuthor = result.content.flatMap { it.authors }.find { it.surname == cervantes.surname }
        assertNotNull(foundAuthor)
    }

    @Test
    fun `searchBook finds by partial author surname`() {
        val partial = cervantes.surname.take(3)
        val result = bookRepository.searchBook(partial, null, PageRequest.of(0, 10, Sort.by("title")))

        val foundAuthor = result.content.flatMap { it.authors }.find { it.surname.contains(partial) }
        assertNotNull(foundAuthor)
    }

    @Test
    fun `searchBook finds by exact ISBN`() {
        val result = bookRepository.searchBook(book3.isbn, null, PageRequest.of(0, 10))

        assertEquals(1, result.totalElements)
        val foundBook = result.content.find { it.isbn == book3.isbn }
        assertNotNull(foundBook)
    }

    @Test
    fun `findBookByIsbn returns book`() {
        val result = bookRepository.findBookByIsbn(book2.isbn)

        assertNotNull(result)
        val foundAuthor = result.authors.find { it.surname == book2.authors.first().surname }
        assertNotNull(foundAuthor)
    }

    @Test
    fun `findByTitleAuthorNameOrAuthorSurnameOrIsbn returns second page of results`() {
        val pageSize = 2
        val page1 = PageRequest.of(0, pageSize, Sort.by("title"))
        val page2 = PageRequest.of(1, pageSize, Sort.by("title"))

        val firstPageResults = bookRepository.searchBook("", null, page1)
        val secondPageResults = bookRepository.searchBook("", null, page2)

        assertEquals(pageSize.toLong(), firstPageResults.content.size.toLong())
        assertEquals(pageSize.toLong(), secondPageResults.content.size.toLong())

        val titlesPage1 = firstPageResults.content.map { it.title }
        val titlesPage2 = secondPageResults.content.map { it.title }

        Assertions.assertTrue(titlesPage2.none { it in titlesPage1 })
    }
}
