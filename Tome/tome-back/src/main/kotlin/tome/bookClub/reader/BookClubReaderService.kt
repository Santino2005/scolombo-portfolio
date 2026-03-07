package tome.bookClub.reader

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tome.Auth0ManagementService
import tome.bookClub.BookClub
import tome.bookClub.BookClubMemberDTO
import tome.bookClub.BookClubRepo
import tome.bookClub.reader.BookClubReader
import tome.bookClub.reader.BookClubReaderRepo
import tome.bookClub.reader.JoinBookClubDto
import tome.exceptions.factories.ExceptionsFactory
import java.util.UUID

@Service
class BookClubReaderService(
    private val repo: BookClubReaderRepo,
    private val bookClubRepo: BookClubRepo,
    private val exceptionsFactory: ExceptionsFactory,
    private val managementService: Auth0ManagementService,
) {
    fun createBookClubReader(
        bookClub: BookClub,
        userId: String,
    ): BookClubReader = repo.save(BookClubReader(bookClub = bookClub, userId = userId, deleted = false))

    fun isMemberOfClub(
        bookClub: BookClub,
        userId: String,
    ): Boolean = repo.existsByBookClubAndUserIdAndDeletedFalse(bookClub, userId)

    fun isMemberOfClub(
        bookClubId: UUID,
        userId: String,
    ): Boolean {
        val bookClub =
            bookClubRepo
                .findById(
                    bookClubId,
                ).orElseThrow { exceptionsFactory.createForbiddenException("User is not member of bookclub") }
        return isMemberOfClub(bookClub, userId)
    }

    @Transactional(readOnly = true)
    fun getBookClubMembers(bookClubId: UUID): List<String> =
        repo
            .findByBookClub_IdAndDeletedFalse(bookClubId)
            .map { it.userId }

    fun countBookClubMembers(bookClubId: UUID): Long = repo.countByBookClub_Id(bookClubId)

    @Transactional(readOnly = true)
    fun getBookClubsForUser(userId: String): List<BookClub> =
        repo
            .findByUserIdAndDeletedFalse(userId)
            .map { it.bookClub }

    fun shareBookClub(
        id: UUID,
        userId: String,
    ): JoinBookClubDto {
        if (bookClubRepo.findById(id).isEmpty) throw exceptionsFactory.createNotFoundException("BookClub not found")
        if (repo.findByUserIdAndBookClub_IdAndDeletedFalse(userId, id) ==
            null
        ) {
            throw exceptionsFactory.createForbiddenException("User is not a member of this book club")
        }
        return JoinBookClubDto("/book-clubs/$id/join")
    }

    fun joinBookClubReader(
        id: UUID,
        userId: String,
    ) {
        val bookClub = bookClubRepo.findById(id).orElseThrow { exceptionsFactory.createNotFoundException("BookClub not found") }
        val bookClubReader = repo.findByUserIdAndBookClub_IdAndDeletedFalse(userId, id)
        if (bookClubReader != null) {
            throw exceptionsFactory.createConflictException("Already in this Book Club")
        }
        repo.save(BookClubReader(bookClub = bookClub, userId = userId, deleted = false))
    }

    @Transactional(readOnly = true)
    fun getBookClubMembersData(bookClubId: UUID): List<BookClubMemberDTO> {
        val membersId = getBookClubMembers(bookClubId)
        val members = mutableListOf<BookClubMemberDTO>()

        for (memberId in membersId) {
            val userResponse = managementService.getUserById(memberId)
            val user = userResponse.body
            if (user != null) {
                val memberDTO =
                    BookClubMemberDTO(
                        id = memberId,
                        name = user.name,
                        picture = user.picture,
                    )
                members.add(memberDTO)
            }
        }

        return members
    }
}
