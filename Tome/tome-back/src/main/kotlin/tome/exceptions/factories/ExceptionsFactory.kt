package tome.exceptions.factories

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import tome.exceptions.BasicHttpException

@Component
class ExceptionsFactory : ExceptionsFactoryInterface {
    override fun createBadRequestException(message: String): BasicHttpException = BasicHttpException(HttpStatus.BAD_REQUEST, message)

    override fun createUnauthorizedException(message: String): BasicHttpException = BasicHttpException(HttpStatus.UNAUTHORIZED, message)

    override fun createForbiddenException(message: String): BasicHttpException = BasicHttpException(HttpStatus.FORBIDDEN, message)

    override fun createNotFoundException(message: String): BasicHttpException = BasicHttpException(HttpStatus.NOT_FOUND, message)

    override fun createConflictException(message: String): BasicHttpException = BasicHttpException(HttpStatus.CONFLICT, message)

    override fun createTooManyRequestsException(message: String): BasicHttpException =
        BasicHttpException(HttpStatus.TOO_MANY_REQUESTS, message)

    override fun createInternalServerError(message: String): BasicHttpException =
        BasicHttpException(HttpStatus.INTERNAL_SERVER_ERROR, message)

    override fun createServiceUnavailableException(message: String): BasicHttpException =
        BasicHttpException(HttpStatus.SERVICE_UNAVAILABLE, message)

    override fun createGatewayTimeoutException(message: String): BasicHttpException =
        BasicHttpException(HttpStatus.GATEWAY_TIMEOUT, message)
}
