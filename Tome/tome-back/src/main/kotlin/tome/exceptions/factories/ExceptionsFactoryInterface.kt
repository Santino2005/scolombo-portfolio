package tome.exceptions.factories

import tome.exceptions.BasicHttpException

interface ExceptionsFactoryInterface {
    // 4xx
    // 400: For malformed or invalid requests
    fun createBadRequestException(message: String): BasicHttpException

    // 401: For when the client is not authenticated
    fun createUnauthorizedException(message: String): BasicHttpException

    // 403: For when client does not have the required permissions
    fun createForbiddenException(message: String): BasicHttpException

    // 404: For when a resource is not found
    fun createNotFoundException(message: String): BasicHttpException

    // 409: For when the data used to create a resource collides with an existing resource (Two users with the same email)
    fun createConflictException(message: String): BasicHttpException

    // 429: For when the client sends to many requests in a short span of time
    fun createTooManyRequestsException(message: String): BasicHttpException

    // 5xx
    // 500: For generic server errors
    fun createInternalServerError(message: String): BasicHttpException

    // 503: For when the service is unavailable
    fun createServiceUnavailableException(message: String): BasicHttpException

    // 504: For when the proxy/gateway times out waiting (For when calling external APIs)
    fun createGatewayTimeoutException(message: String): BasicHttpException
}
