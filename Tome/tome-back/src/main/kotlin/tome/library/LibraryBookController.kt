package tome.library

import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import tome.books.toSearchLibraryBookDTO
import tome.exceptions.factories.ExceptionsFactory
import tome.library.dto.LibraryBookDTO
import tome.library.dto.LibraryBookStatusTypeDTO
import tome.library.status.LibraryBookStatusType
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.UUID

@RequestMapping("/libraries")
@RestController
class LibraryBookController(
    private val libraryBookService: LibraryBookService,
) {

    //Progress Status and Update
    @PostMapping("/books/{bookId}")
    fun libraryBook(
        @PathVariable bookId: UUID,
        @RequestBody @Valid libraryBook: LibraryBookStatusTypeDTO,
        @AuthenticationPrincipal jwt: Jwt,
    ): LibraryBookStatusTypeDTO {
        val startedAtDate =
            libraryBook.startedAt?.let {
                try {
                    LocalDate.parse(it)
                } catch (_: DateTimeParseException) {
                    throw ExceptionsFactory().createBadRequestException("Invalid date format for startedAt. Please use YYYY-MM-DD.")
                }
            }

        val finishedAtDate =
            libraryBook.finishedAt?.let {
                try {
                    LocalDate.parse(it)
                } catch (_: DateTimeParseException) {
                    throw ExceptionsFactory().createBadRequestException("Invalid date format for finishedAt. Please use YYYY-MM-DD.")
                }
            }

        return toLibraryBookStatus(
            libraryBookService.createLibraryBook(
                bookId,
                libraryBook.readingStatus,
                libraryBook.currentPage,
                startedAtDate,
                jwt.subject,
                finishedAtDate,
            ),
        )
    }
    @GetMapping("/personal")
    fun getPersonalLibrary(
        @AuthenticationPrincipal jwt: Jwt,
    ): Map<LibraryBookStatusType, List<LibraryBookDTO>> {
        val newStatus = libraryBookService.getLibraryBookByStatus(LibraryBookStatusType.New_Status, jwt.subject)
        val readingBooks = libraryBookService.getLibraryBookByStatus(LibraryBookStatusType.READING, jwt.subject)
        val dNFBooks = libraryBookService.getLibraryBookByStatus(LibraryBookStatusType.DNF, jwt.subject)
        val wantToReadBook = libraryBookService.getLibraryBookByStatus(LibraryBookStatusType.WANT_TO_READ, jwt.subject)
        val readBook = libraryBookService.getLibraryBookByStatus(LibraryBookStatusType.READ, jwt.subject)

        return mapOf(
            LibraryBookStatusType.New_Status to convertToLibraryBook(newStatus),
            LibraryBookStatusType.READING to convertToLibraryBook(readingBooks),
            LibraryBookStatusType.DNF to convertToLibraryBook(dNFBooks),
            LibraryBookStatusType.WANT_TO_READ to convertToLibraryBook(wantToReadBook),
            LibraryBookStatusType.READ to convertToLibraryBook(readBook),
            // Add other statuses as needed
        )
    }

    //Visualizar libros en progreso
    @GetMapping()
    fun filterLibraryBookByStatus(
        @RequestParam filter: LibraryBookStatusType,
        @AuthenticationPrincipal jwt: Jwt,
    ): List<LibraryBookDTO> =
        convertToLibraryBook(
            libraryBookService
                .getLibraryBookByStatus(filter, jwt.subject),
        )




    @DeleteMapping("/books/{bookId}")
    fun deleteLibraryBook(
        @PathVariable bookId: UUID,
        @AuthenticationPrincipal jwt: Jwt,
    ): ResponseEntity<Void> {
        libraryBookService.deleteLibraryBook(jwt.subject, bookId)
        return ResponseEntity.noContent().build()
    }

    //Buscar libro en mi biblioteca
    @GetMapping("/personal", params = ["search"])
    fun getBooks(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestParam search: String,
        @RequestParam tags: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): Page<LibraryBookDTO> =
        if (search.isBlank()) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Search term cannot be blank",
            )
        } else {
            val pageable = PageRequest.of(page, size)
            val tagList = tags?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }
            libraryBookService
                .findByLibraryBookTitleAuthorNameOrAuthorSurnameOrIsbnOrTags(jwt.subject, search, tagList, pageable)
                .map(::toSearchLibraryBookDTO)
        }
}
