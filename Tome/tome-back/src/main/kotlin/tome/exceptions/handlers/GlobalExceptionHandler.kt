package tome.exceptions.handlers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import tome.exceptions.ApiError
import tome.exceptions.BasicHttpException
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BasicHttpException::class)
    fun handleBasicHttpException(e: BasicHttpException): ResponseEntity<ApiError> {
        val error: ApiError = ApiError(LocalDateTime.now(), e.message)
        return ResponseEntity(error, e.status)
    }
}
