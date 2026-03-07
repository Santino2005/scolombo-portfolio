package tome

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponseWrapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@WebFilter("/*")
class RequestLoggingFilter : Filter {
    private val logger = LoggerFactory.getLogger(RequestLoggingFilter::class.java)

    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain,
    ) {
        if (request is HttpServletRequest && response is HttpServletResponse) {
            val wrapped = StatusCaptureResponseWrapper(response)
            logger.info("Request: {} {}", request.method, request.requestURI)
            chain.doFilter(request, wrapped)
            logger.info("Response: {} {}", wrapped.getStatus(), request.requestURI)
        } else {
            chain.doFilter(request, response)
        }
    }

    override fun init(filterConfig: FilterConfig?) {}

    override fun destroy() {}

    private class StatusCaptureResponseWrapper(
        response: HttpServletResponse,
    ) : HttpServletResponseWrapper(response) {
        private var _status: Int = response.status

        override fun getStatus(): Int = _status

        override fun setStatus(sc: Int) {
            super.setStatus(sc)
            _status = sc
        }

        override fun sendError(sc: Int) {
            super.sendError(sc)
            _status = sc
        }

        override fun sendError(
            sc: Int,
            msg: String?,
        ) {
            super.sendError(sc, msg)
            _status = sc
        }

        override fun sendRedirect(location: String) {
            super.sendRedirect(location)
            _status = SC_MOVED_TEMPORARILY
        }
    }
}
