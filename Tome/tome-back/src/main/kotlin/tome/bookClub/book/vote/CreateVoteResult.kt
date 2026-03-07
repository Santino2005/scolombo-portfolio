package tome.bookClub.book.vote

data class CreateVoteResult(
    val vote: BookClubBookVote,
    val bookIsWinner: Boolean,
)
