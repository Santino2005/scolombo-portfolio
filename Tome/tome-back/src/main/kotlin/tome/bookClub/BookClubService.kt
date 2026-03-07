package tome.bookClub

import org.springframework.stereotype.Service
import tome.bookClub.reader.BookClubReaderService
import tome.exceptions.factories.ExceptionsFactoryInterface
import java.util.Base64
import java.util.UUID

@Service
class BookClubService(
    private val bookClubRepo: BookClubRepo,
    private val bookClubReaderService: BookClubReaderService,
    private val exceptionsFactory: ExceptionsFactoryInterface,
) {
    fun createBookClub(
        name: String,
        imgBlob: String,
        creatorId: String,
    ): BookClub {
        require(imgBlob.isNotBlank()) { "Cant empty image" }
        val imgBytes =
            try {
                val cleanBase64 = imgBlob.substringAfter("base64,", imgBlob)
                Base64.getDecoder().decode(cleanBase64)
            } catch (e: IllegalArgumentException) {
                throw exceptionsFactory.createBadRequestException("Image is not in a valid Base64")
            }
        if (imgBytes.size > 400 * 1024) {
            throw exceptionsFactory.createBadRequestException("Image is not in a valid Base64")
        }
        val club = bookClubRepo.save(BookClub(null, name = name, imgBlob = imgBytes))
        bookClubReaderService.createBookClubReader(club, creatorId)
        return club
    }

    fun getBookClubDetail(
        id: UUID,
        userId: String,
    ): BookClub {
        val bookClub =
            bookClubRepo.findById(id).orElseThrow {
                exceptionsFactory.createNotFoundException("BookClub not found")
            }

        val isMember = bookClubReaderService.isMemberOfClub(bookClub, userId)
        if (!isMember) {
            throw exceptionsFactory.createUnauthorizedException("User is not a member of this book club")
        }
        return bookClub
    }

    fun getBookClubById(id: UUID): BookClub =
        bookClubRepo.findById(id).orElseThrow { exceptionsFactory.createNotFoundException("That book club does not exists.") }
}
