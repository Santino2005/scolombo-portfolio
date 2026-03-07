package tome.bookClub

data class JoinBookClubDTO(
    val bookClubName: String,
    val members: List<BookClubMemberDTO>,
    val imageBase64: String?,
)
