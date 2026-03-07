package tome.bookClub

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class BookClubBodyDTO(
    @field:NotBlank(message = "Cant empty name")
    @field:Size(min = 1, max = 50, message = "name have between 1 and 50 characters")
    val name: String,
    val imageBase64: String,
)
