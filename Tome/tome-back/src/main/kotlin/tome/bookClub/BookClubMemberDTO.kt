package tome.bookClub

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

class BookClubMemberDTO(
    val id: String,
    @field:NotBlank(message = "Cant empty name")
    @field:Size(min = 1, max = 50, message = "name have between 1 and 50 characters")
    val name: String,
    val picture: String,
)
