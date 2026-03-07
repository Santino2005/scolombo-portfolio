package tome.books

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tome.books.dto.BookDTO
import tome.books.dto.SearchDTO
import tome.exceptions.factories.ExceptionsFactory
import java.util.UUID

@RequestMapping("/books")
@RestController
class BookController(
    private val bookService: BookService,
) {
    @GetMapping(params = ["search"])
    fun searchBooks(
        @RequestParam search: String,
        @RequestParam tags: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "title") sort: String,
        @RequestParam(defaultValue = "asc") direction: String,
    ): Page<SearchDTO> {
        val sorting: Sort = Sort.by(Sort.Direction.fromString(direction), sort)
        val pageable: Pageable = PageRequest.of(page, size, sorting)
        val tagList = tags?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }
        val bookPage: Page<Book> = bookService.findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(search, tagList, pageable)
        return bookPage.map { SearchDTO(requireNotNull(it.id) { "Book uuid on db is null" }, it.title, it.authors, it.coverUrl) }
    }

    @GetMapping("/{id}")
    fun getBookById(
        @PathVariable id: UUID,
        @AuthenticationPrincipal jwt: Jwt,
    ): BookDTO {
        val libraryAndBookPair = bookService.findByIdAndUserID(id, jwt.subject)
        val book = libraryAndBookPair.first
        val libraryBook = libraryAndBookPair.second
        if (book == null) {
            throw ExceptionsFactory().createNotFoundException("Book with $id not found")
        }
        return toBookDTO(
            book,
            libraryBook,
        )
    }
}
