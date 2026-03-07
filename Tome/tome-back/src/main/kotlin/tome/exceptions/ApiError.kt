package tome.exceptions

import java.time.LocalDateTime

data class ApiError(
    val timestamp: LocalDateTime,
    val message: String,
    val details: String? = null,
)
