package tome.bookTesting

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.isNull
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tome.books.Book
import tome.books.BookController
import tome.books.BookService
import tome.books.author.Author
import tome.books.languageApi.Language
import tome.books.publisherApi.Publisher
import tome.books.tag.Tag
import tome.library.LibraryBook
import tome.library.status.LibraryBookStatusType
import java.time.LocalDate
import java.util.UUID
import kotlin.test.assertFailsWith

@ExtendWith(SpringExtension::class)
@WebMvcTest(BookController::class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = ["AUTH0_ISSUER_URI=https://my-account.com/"])
class BookControllerTest(
    @Autowired private val mockMvc: MockMvc,
) {
    @MockitoBean
    private lateinit var jwtDecoder: JwtDecoder

    @MockitoBean
    private lateinit var bookService: BookService
    private lateinit var controller: BookController

    @BeforeEach
    fun setup() {
        controller = BookController(bookService)
    }

    private val english = Language(null, "English")
    private val author = Author(id = null, name = "George", surname = "Orwell", fullName = "George Orwell")
    private val publisher = Publisher(id = null, name = "Secker & Warburg")
    private val harperCollins = Publisher(null, "HarperCollins")
    private val political = Tag(id = null, name = "Political fiction")
    private val dystopian = Tag(null, "Dystopian")
    private val penguin = Publisher(null, "Penguin Books")
    private val fiction = Tag(null, "Fiction")

    private fun createAuthor(fullName: String) =
        Author(UUID.randomUUID(), name = fullName.split(" ").first(), surname = fullName.split(" ").last(), fullName = fullName)

    private fun createBook(
        id: UUID?,
        title: String,
        isbn: String,
        authors: List<Author>,
        publisher: Publisher,
        tags: List<Tag>,
    ) = Book(
        id = id,
        title = title,
        isbn = isbn,
        releasedDate = LocalDate.now(),
        pages = 350,
        coverUrl = "cover_$isbn.jpg",
        synopsis = "Synopsis for $title",
        language = english,
        publisher = publisher,
        tags = tags,
        authors = authors,
    )

    private fun jwtForUser(userId: String) =
        Jwt
            .withTokenValue("fake-token")
            .header("alg", "RS256")
            .claim("sub", userId)
            .build()

    @Test
    fun `should return 200 and book details when book exists`() {
        val bookId = UUID.randomUUID()
        val userId = "auth0|user123"
        val jwt = jwtForUser(userId)

        whenever(jwtDecoder.decode(any())).thenReturn(jwt)

        val title = "1984"
        val isbn = "9780451524935"
        val releasedDate = LocalDate.of(1949, 6, 8)
        val pages = 328
        val coverUrl = "https://covers.openlibrary.org/1984.jpg"
        val synopsis = "A dystopian social science fiction novel and cautionary tale."

        val book =
            Book(
                id = bookId,
                title = title,
                isbn = isbn,
                releasedDate = releasedDate,
                pages = pages,
                coverUrl = coverUrl,
                synopsis = synopsis,
                language = english,
                publisher = publisher,
                tags = listOf(dystopian, political),
                authors = listOf(author),
            )

        val libraryBook =
            LibraryBook(
                userId = userId,
                status = LibraryBookStatusType.READ,
                book = book,
                currentPage = pages,
            )

        whenever(bookService.findByIdAndUserID(bookId, userId))
            .thenReturn(book to libraryBook)

        mockMvc
            .perform(
                get("/books/$bookId")
                    .header("Authorization", "Bearer ${jwt.tokenValue}"),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(book.id.toString()))
            .andExpect(jsonPath("$.title").value(title))
            .andExpect(jsonPath("$.isbn").value(isbn))
    }

    @Test
    fun `should return 404 when book not found`() {
        val bookId = UUID.randomUUID()
        val userId = "auth0|user404"
        val jwt = jwtForUser(userId)

        whenever(jwtDecoder.decode(any())).thenReturn(jwt)
        whenever(bookService.findByIdAndUserID(bookId, userId))
            .thenReturn(null to null)

        mockMvc
            .perform(
                get("/books/$bookId")
                    .header("Authorization", "Bearer ${jwt.tokenValue}"),
            ).andExpect(status().isNotFound)
    }

    @Test
    fun `should return 401 when no authorization header provided`() {
        val bookId = UUID.randomUUID()

        mockMvc
            .perform(get("/books/$bookId"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should return paginated search results`() {
        val search = "Lord"
        val jwt = jwtForUser("auth0|user123")
        whenever(jwtDecoder.decode(any())).thenReturn(jwt)

        val author = Author(null, "J.R.R.", "Tolkien", fullName = "J.R.R. Tolkien")
        val language = Language(null, "English")
        val publisher = Publisher(null, "Allen & Unwin")
        val fantasy = Tag(null, "Fantasy")

        val book1 =
            Book(
                id = UUID.randomUUID(),
                title = "The Lord of the Rings",
                isbn = "9780261102385",
                releasedDate = LocalDate.of(1954, 7, 29),
                pages = 1216,
                coverUrl = "https://covers.openlibrary.org/lotr.jpg",
                synopsis = "An epic high fantasy novel.",
                language = language,
                publisher = publisher,
                tags = listOf(fantasy),
                authors = listOf(author),
            )

        val book2 =
            Book(
                id = UUID.randomUUID(),
                title = "The Lord of the Rings: The Return of the King",
                isbn = "9780261103597",
                releasedDate = LocalDate.of(1955, 10, 20),
                pages = 416,
                coverUrl = "https://covers.openlibrary.org/lotr3.jpg",
                synopsis = "The last volume of The Lord of the Rings.",
                language = language,
                publisher = publisher,
                tags = listOf(fantasy),
                authors = listOf(author),
            )

        val totalBooks = listOf(book1, book2)
        val pageable =
            PageRequest
                .of(0, 10)
        val page =
            PageImpl(totalBooks, pageable, totalBooks.size.toLong())

        whenever(bookService.findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(eq(search), isNull(), any()))
            .thenReturn(page)
            .thenReturn(page)

        mockMvc
            .perform(
                get("/books")
                    .param("search", search)
                    .param("page", "0")
                    .param("size", "10")
                    .param("sort", "title")
                    .param("direction", "asc")
                    .header("Authorization", "Bearer ${jwt.tokenValue}"),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(totalBooks.size))
            .andExpect(jsonPath("$.content[0].title").value(book1.title))
            .andExpect(jsonPath("$.content[0].author[0].fullName").value(author.fullName))
            .andExpect(jsonPath("$.content[0].coverUrl").value(book1.coverUrl))
            .andExpect(jsonPath("$.content[1].title").value(book2.title))
    }

    @Test
    fun `should return second page of results`() {
        val search = "The"
        val jwt = jwtForUser("auth0|user123")
        whenever(jwtDecoder.decode(any())).thenReturn(jwt)

        val author = Author(null, "George", "Orwell", "George Orwell")
        val language = Language(null, "English")
        val publisher = Publisher(null, "Secker & Warburg")
        val dystopian = Tag(null, "Dystopian")

        val books =
            (1..20).map {
                Book(
                    id = UUID.randomUUID(),
                    title = "The Book $it",
                    isbn = "ISBN-$it",
                    releasedDate = LocalDate.now(),
                    pages = 300,
                    coverUrl = "https://covers.openlibrary.org/book$it.jpg",
                    synopsis = "Book number $it",
                    language = language,
                    publisher = publisher,
                    tags = listOf(dystopian),
                    authors = listOf(author),
                )
            }

        val pageSize = 10
        val currentPage = 1
        val expectedBooks = books.drop(currentPage * pageSize).take(pageSize)
        val pageable =
            PageRequest
                .of(currentPage, pageSize)
        val page =
            PageImpl(expectedBooks, pageable, books.size.toLong())

        whenever(bookService.findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(eq(search), isNull(), any()))
            .thenReturn(page)
            .thenReturn(page)

        val expectedTitle = "The Book 11"

        mockMvc
            .perform(
                get("/books")
                    .param("search", search)
                    .param("page", currentPage.toString())
                    .param("size", pageSize.toString())
                    .header("Authorization", "Bearer ${jwt.tokenValue}"),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(expectedBooks.size))
            .andExpect(jsonPath("$.totalElements").value(books.size))
            .andExpect(jsonPath("$.content[?(@.title=='$expectedTitle')]").exists())
    }

    @Test
    fun `search by exact title`() {
        val search = "1984"
        val author = createAuthor("George Orwell")
        val book = createBook(UUID.randomUUID(), search, "9780451524935", listOf(author), penguin, listOf(dystopian))
        val page: Page<Book> = PageImpl(listOf(book))
        whenever(bookService.findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(eq(search), isNull(), any())).thenReturn(page)

        val result = controller.searchBooks(search, null, 0, 10, "title", "asc")

        Assertions.assertEquals(page.totalElements, result.totalElements)
        val foundBook = result.content.find { it.title == search }
        Assertions.assertNotNull(foundBook)

        val foundAuthor = foundBook!!.author.find { it.fullName == author.fullName }
        Assertions.assertNotNull(foundAuthor)
        Assertions.assertEquals("George Orwell", foundAuthor!!.fullName)
    }

    @Test
    fun `search by partial title`() {
        val search = "Rings"
        val author = createAuthor("J.R.R. Tolkien")
        val book = createBook(UUID.randomUUID(), "The Lord of the Rings", "9780261102385", listOf(author), harperCollins, listOf(fiction))
        val page: Page<Book> = PageImpl(listOf(book))
        whenever(bookService.findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(eq(search), isNull(), any())).thenReturn(page)

        val result = controller.searchBooks(search, null, 0, 10, "title", "asc")
        val book1984 = result.content.find { it.title.contains(search, ignoreCase = true) }
        Assertions.assertNotNull(book1984)
        Assertions.assertTrue(book1984!!.title.contains(search, ignoreCase = true))
    }

    @Test
    fun `search by exact author name`() {
        val search = "Jane Austen"
        val author = createAuthor(search)
        val book = createBook(UUID.randomUUID(), "Pride and Prejudice", "9780141439518", listOf(author), penguin, listOf(fiction))
        val page: Page<Book> = PageImpl(listOf(book))
        whenever(bookService.findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(eq(search), isNull(), any())).thenReturn(page)

        val result = controller.searchBooks(search, null, 0, 10, "title", "asc")

        val foundBook =
            result.content.find { book ->
                book.author.any { it.fullName == author.fullName }
            }
        Assertions.assertNotNull(foundBook)

        val foundAuthor = foundBook!!.author.find { it.fullName == author.fullName }
        Assertions.assertNotNull(foundAuthor)
        Assertions.assertEquals(author.fullName, foundAuthor!!.fullName)
    }

    @Test
    fun `search by partial author name`() {
        val search = "Aust"
        val author = createAuthor("Jane Austen")
        val book = createBook(UUID.randomUUID(), "Sense and Sensibility", "9780141199672", listOf(author), penguin, listOf(fiction))
        val page: Page<Book> = PageImpl(listOf(book))
        whenever(bookService.findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(eq(search), isNull(), any())).thenReturn(page)

        val result = controller.searchBooks(search, null, 0, 10, "title", "asc")

        val foundBook =
            result.content.find { book ->
                book.author.any { it.fullName.contains(search, ignoreCase = true) }
            }
        Assertions.assertNotNull(foundBook)

        val foundAuthor = foundBook!!.author.find { it.fullName.contains(search, ignoreCase = true) }
        Assertions.assertNotNull(foundAuthor)
        Assertions.assertTrue(foundAuthor!!.fullName.contains(search, ignoreCase = true))
    }

    @Test
    fun `search by exact ISBN`() {
        val isbn = "9780439023481"
        val author = createAuthor("Suzanne Collins")
        val book =
            createBook(
                UUID.randomUUID(),
                "The Hunger Games",
                isbn,
                listOf(author),
                publisher = Publisher(null, "Scholastic Press"),
                tags = listOf(dystopian),
            )
        val page: Page<Book> = PageImpl(listOf(book))
        whenever(
            bookService
                .findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(eq(isbn), isNull(), any()),
        ).thenReturn(page)

        val result = controller.searchBooks(isbn, null, 0, 10, "title", "asc")

        val foundBook = result.content.find { it.id == book.id }
        Assertions.assertNotNull(foundBook)
        Assertions.assertEquals(book.id, foundBook!!.id)
    }

    @Test
    fun `getBookById returns BookDTO when book exists`() {
        val id = UUID.randomUUID()
        val jwt = FakeJwt("user-123")
        val author = createAuthor("J.R.R. Tolkien")
        val book = createBook(id, "The Hobbit", "9780261103344", listOf(author), harperCollins, listOf(fiction))
        val libraryBook = LibraryBook(userId = jwt.subject, status = LibraryBookStatusType.READING, book = book)
        whenever(bookService.findByIdAndUserID(id, jwt.subject)).thenReturn(book to libraryBook)

        val dto = controller.getBookById(id, jwt)

        Assertions.assertEquals(book.title, dto.title)
        Assertions.assertEquals(book.isbn, dto.isbn)

        val matchingAuthor = dto.authors.find { it.fullName == author.fullName }
        Assertions.assertNotNull(matchingAuthor)
        Assertions.assertEquals(author.fullName, matchingAuthor!!.fullName)
    }

    @Test
    fun `getBookById throws NotFound when book does not exist`() {
        val id = UUID.randomUUID()
        val jwt = FakeJwt("user-123")
        whenever(bookService.findByIdAndUserID(id, jwt.subject)).thenReturn(null to null)

        val exception = assertFailsWith<RuntimeException> { controller.getBookById(id, jwt) }

        Assertions.assertTrue(exception.message!!.contains(id.toString()))
    }

    @Test
    fun `searchBooks throws IllegalArgumentException when book id is null`() {
        val search = "Unknown Title"
        val author = createAuthor("Unknown Author")
        val book = createBook(null, search, "0000000000", listOf(author), penguin, listOf(fiction))
        val page: Page<Book> = PageImpl(listOf(book))
        whenever(bookService.findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(eq(search), isNull(), any())).thenReturn(page)

        val exception =
            assertFailsWith<IllegalArgumentException> {
                controller.searchBooks(search, null, 0, 10, "title", "asc")
            }

        val expectedMessage = "Book uuid on db is null"
        Assertions.assertEquals(expectedMessage, exception.message)
    }

    private data class FakeJwt(
        val fakeSub: String,
    ) : Jwt(
            "token",
            null,
            null,
            mapOf("alg" to "none"),
            mapOf("sub" to fakeSub),
        ) {
        val jwtSubject: String get() = fakeSub
    }
}
