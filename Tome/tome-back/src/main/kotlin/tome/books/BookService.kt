package tome.books

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import tome.library.LibraryBook
import tome.library.LibraryBookService
import java.util.UUID

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val libraryBookService: LibraryBookService,
) {
    fun findByTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(
        search: String,
        tags: List<String>?,
        pageable: Pageable,
    ): Page<Book> = bookRepository.searchBook(search, tags, pageable)

    fun findById(id: UUID): Book? = bookRepository.findById(id).orElse(null)

    fun findByIdAndUserID(
        id: UUID,
        userId: String,
    ): Pair<Book?, LibraryBook?> = Pair(findById(id), libraryBookService.getLibraryBook(userId, id))
}
