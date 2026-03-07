package tome.bookClub

import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tome.bookClub.book.BookClubBookController
import tome.bookClub.reader.BookClubDTO
import tome.bookClub.reader.BookClubDetailsDTO
import tome.bookClub.reader.BookClubReaderService
import tome.bookClub.reader.JoinBookClubDto
import tome.bookClub.reader.toBookClubDTO
import tome.bookClub.reader.toBookClubDetailsDTO
import java.util.Base64
import java.util.UUID

@RestController
@RequestMapping("book-clubs")
class BookClubController(
    val bookClubService: BookClubService,
    val bookClubReaderService: BookClubReaderService,
    val bookClubBookController: BookClubBookController,
) {
    //Create Book Club
    @PostMapping
    fun createBookClub(
        @AuthenticationPrincipal jwt: Jwt,
        @Valid @RequestBody club: BookClubBodyDTO,
    ): BookClubDTO {
        val bookClub = bookClubService.createBookClub(club.name, club.imageBase64, jwt.subject)
        return toBookClubDTO(bookClub)
    }

    @GetMapping("/{id}")
    fun getBookClubDetail(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable id: UUID,
    ): BookClubDetailsDTO {
        val bookClub = bookClubService.getBookClubDetail(id, jwt.subject)
        val currentBook = bookClubBookController.readCurrent(jwt, id)
        return toBookClubDetailsDTO(bookClub, currentBook)
    }

    @GetMapping
    fun getUserBookClubs(
        @AuthenticationPrincipal jwt: Jwt,
    ): List<BookClubDetailsDTO> {
        val clubs = bookClubReaderService.getBookClubsForUser(jwt.subject)
        return clubs.map { toBookClubDetailsDTO(it) }
    }

    //Compartir grupo de lectura
    @GetMapping("/{id}/join")
    fun joinBookClub(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable id: UUID,
    ): JoinBookClubDto = bookClubReaderService.shareBookClub(id, jwt.subject)

    //Unirse a grupo de lectura
    @PostMapping("/{id}/join")
    fun joinBookClubAsUser(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable id: UUID,
    ) = bookClubReaderService
        .joinBookClubReader(id, jwt.subject)

    @GetMapping("/{bookClubId}/join/data")
    fun getBookClubDataToJoin(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable bookClubId: UUID,
    ): JoinBookClubDTO {
        val bookClub = bookClubService.getBookClubById(bookClubId)
        val members = bookClubReaderService.getBookClubMembersData(bookClub.id!!)
        val imgBase64 = Base64.getEncoder().encodeToString(bookClub.imgBlob)
        return JoinBookClubDTO(
            bookClub.name,
            members = members,
            imageBase64 = imgBase64,
        )
    }
}
