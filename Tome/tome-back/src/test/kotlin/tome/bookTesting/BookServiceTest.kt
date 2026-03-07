package tome.bookTesting

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.isNull
import org.mockito.kotlin.whenever
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import tome.books.Book
import tome.books.BookRepository
import tome.books.BookService
import tome.books.author.Author
import tome.books.languageApi.Language
import tome.books.publisherApi.Publisher
import tome.books.tag.Tag
import tome.library.LibraryBook
import tome.library.LibraryBookService
import tome.library.status.LibraryBookStatusType
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class BookServiceTest {
    @Mock
    private lateinit var bookRepository: BookRepository

    @Mock
    private lateinit var libraryBookService: LibraryBookService

    @InjectMocks
    private lateinit var bookService: BookService

    private val language = Language(null, "English")
    private val publisher = Publisher(null, "Publisher")
    private val tag = Tag(null, "Fiction")

    private fun createAuthor(fullName: String) = Author(UUID.randomUUID(), name = "", surname = "", fullName = fullName)

    private fun createBook(
        id: UUID?,
        title: String,
        isbn: String,
        authors: List<Author>,
    ) = Book(
        id = id,
        title = title,
        isbn = isbn,
        releasedDate = LocalDate.now(),
        pages = 200,
        coverUrl = "cover.jpg",
        synopsis = "Synopsis for $title",
        language = language,
        publisher = publisher,
        tags = listOf(tag),
        authors = authors,
    )

    @Test
    fun `findByTitleAuthorNameOrAuthorSurnameOrIsbn returns books page`() {
        val search = "Pride"
        val author = createAuthor("Jane Austen")
        val book = createBook(UUID.randomUUID(), "Pride and Prejudice", "9780141199078", listOf(author))
        val page: Page<Book> = PageImpl(listOf(book))

        whenever(bookRepository.searchBook(eq(search), isNull<List<String>>(), any<Pageable>()))
            .thenReturn(page)

        val result = bookService.findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(search, null, PageRequest.of(0, 10))

        Assertions.assertEquals(1, result.totalElements)

        val foundBook = result.content.find { it.title == book.title }
        Assertions.assertNotNull(foundBook)

        val foundAuthor = foundBook!!.authors.find { it.fullName == author.fullName }
        Assertions.assertNotNull(foundAuthor)
        Assertions.assertEquals(author.fullName, foundAuthor!!.fullName)
    }

    @Test
    fun `findById returns book when exists`() {
        val author = createAuthor("George Orwell")
        val book = createBook(UUID.randomUUID(), "1984", "9780451524935", listOf(author))
        whenever(bookRepository.findById(book.id!!)).thenReturn(Optional.of(book))

        val result = bookService.findById(book.id!!)

        Assertions.assertNotNull(result)

        val foundAuthor = result!!.authors.find { it.fullName == author.fullName }
        Assertions.assertNotNull(foundAuthor)
        Assertions.assertEquals(author.fullName, foundAuthor!!.fullName)
    }

    @Test
    fun `findById returns null when book does not exist`() {
        val id = UUID.randomUUID()
        whenever(bookRepository.findById(id)).thenReturn(Optional.empty())

        val result = bookService.findById(id)

        Assertions.assertNull(result)
    }

    @Test
    fun `findByIdAndUserID returns book and libraryBook`() {
        val userId = "user-123"
        val author = createAuthor("Miguel de Cervantes")
        val book = createBook(UUID.randomUUID(), "Don Quijote", "9788491050294", listOf(author))
        val libraryBook = LibraryBook(userId = userId, status = LibraryBookStatusType.READ, book = book)

        whenever(bookRepository.findById(book.id!!)).thenReturn(Optional.of(book))
        whenever(libraryBookService.getLibraryBook(userId, book.id!!)).thenReturn(libraryBook)

        val (foundBook, foundLibraryBook) = bookService.findByIdAndUserID(book.id!!, userId)

        Assertions.assertEquals(book, foundBook)
        Assertions.assertEquals(libraryBook, foundLibraryBook)
    }

    @Test
    fun `findByIdAndUserID returns book but libraryBook is null`() {
        val userId = "user-123"
        val author = createAuthor("Harper Lee")
        val book = createBook(UUID.randomUUID(), "To Kill a Mockingbird", "9780061120084", listOf(author))

        whenever(bookRepository.findById(book.id!!)).thenReturn(Optional.of(book))
        whenever(libraryBookService.getLibraryBook(userId, book.id!!)).thenReturn(null)

        val (foundBook, foundLibraryBook) = bookService.findByIdAndUserID(book.id!!, userId)

        Assertions.assertEquals(book, foundBook)
        Assertions.assertNull(foundLibraryBook)
    }

    @Test
    fun `findByIdAndUserID returns nulls when book does not exist`() {
        val userId = "user-123"
        val id = UUID.randomUUID()
        whenever(bookRepository.findById(id)).thenReturn(Optional.empty())
        whenever(libraryBookService.getLibraryBook(userId, id)).thenReturn(null)

        val (foundBook, foundLibraryBook) = bookService.findByIdAndUserID(id, userId)

        Assertions.assertNull(foundBook)
        Assertions.assertNull(foundLibraryBook)
    }

    @Test
    fun `findByTitleAuthorNameOrAuthorSurnameOrIsbn returns books matching partial title`() {
        val search = "Rings"
        val author = createAuthor("J.R.R. Tolkien")
        val book = createBook(UUID.randomUUID(), "The Lord of the Rings", "9780261102385", listOf(author))
        val page: Page<Book> = PageImpl(listOf(book))

        whenever(bookRepository.searchBook(eq(search), isNull<List<String>>(), any<Pageable>()))
            .thenReturn(page)

        val result = bookService.findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(search, null, PageRequest.of(0, 10))

        Assertions.assertEquals(1, result.totalElements)

        val foundBook = result.content.find { it.title.contains(search, ignoreCase = true) }
        Assertions.assertNotNull(foundBook)

        val foundAuthor = foundBook!!.authors.find { it.fullName == author.fullName }
        Assertions.assertNotNull(foundAuthor)
        Assertions.assertEquals(author.fullName, foundAuthor!!.fullName)
    }

    @Test
    fun `findByTitleAuthorNameOrAuthorSurnameOrIsbn returns books matching partial author`() {
        val search = "Austen"
        val author = createAuthor("Jane Austen")
        val book = createBook(UUID.randomUUID(), "Sense and Sensibility", "9780141199672", listOf(author))
        val page: Page<Book> = PageImpl(listOf(book))

        whenever(bookRepository.searchBook(eq(search), isNull<List<String>>(), any<Pageable>()))
            .thenReturn(page)

        val result = bookService.findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(search, null, PageRequest.of(0, 10))

        Assertions.assertEquals(1, result.totalElements)

        val foundBook = result.content.find { it.authors.any { a -> a.fullName.contains(search, ignoreCase = true) } }
        Assertions.assertNotNull(foundBook)
    }

    @Test
    fun `findByTitleAuthorNameOrAuthorSurnameOrIsbn returns second page of results`() {
        val pageSize = 2
        val allBooks =
            listOf(
                createBook(UUID.randomUUID(), "Pride and Prejudice", "9780141199078", listOf(createAuthor("Jane Austen"))),
                createBook(UUID.randomUUID(), "Sense and Sensibility", "9780141199672", listOf(createAuthor("Jane Austen"))),
                createBook(UUID.randomUUID(), "1984", "9780451524935", listOf(createAuthor("George Orwell"))),
                createBook(UUID.randomUUID(), "Animal Farm", "9780451526342", listOf(createAuthor("George Orwell"))),
            )

        val firstPage = PageImpl(allBooks.take(pageSize), PageRequest.of(0, pageSize), allBooks.size.toLong())
        val secondPage = PageImpl(allBooks.drop(pageSize).take(pageSize), PageRequest.of(1, pageSize), allBooks.size.toLong())

        whenever(bookRepository.searchBook(eq(""), isNull<List<String>>(), any<Pageable>()))
            .thenAnswer { invocation ->
                val pageable = invocation.getArgument<PageRequest>(2) // ✅ índice corregido
                when (pageable.pageNumber) {
                    0 -> firstPage
                    1 -> secondPage
                    else -> PageImpl(emptyList(), pageable, allBooks.size.toLong())
                }
            }

        val result = bookService.findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags("", null, PageRequest.of(1, pageSize))

        Assertions.assertEquals(pageSize, result.content.size)
        Assertions.assertEquals(allBooks.drop(pageSize).take(pageSize).map { it.title }, result.content.map { it.title })
        Assertions.assertEquals(allBooks.size.toLong(), result.totalElements)
    }
}
