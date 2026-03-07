package tome.exceptions

import org.springframework.http.HttpStatus

class BasicHttpException(
    val status: HttpStatus,
    override val message: String,
) : RuntimeException(message)
