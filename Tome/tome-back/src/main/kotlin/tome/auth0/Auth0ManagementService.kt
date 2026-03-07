package tome

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import tome.TokenRequest
import tome.exceptions.factories.ExceptionsFactory
import java.io.File
import java.util.Base64

@Component
class Auth0ManagementService
    @Autowired
    constructor(
        private val rest: RestTemplate = RestTemplate(),
        @Value("\${auth0.issuer.uri}")
        private val auth0Url: String,
        @Value("\${auth0.management.token}")
        private var managementToken: String,
        @Value("\${auth0.management.client.id}")
        private val clientId: String,
        @Value("\${auth0.management.client.secret}")
        private val clientSecret: String,
        @Value("\${auth0.management.audience}")
        private val audience: String,
        @Value("\${auth0.environment.file.path}")
        private val envFilePath: String,
        private val exceptionsFactory: ExceptionsFactory,
    ) {
        fun getUserById(userId: String): ResponseEntity<UserProfileDTO> =
            try {
                val request = HttpEntity<Void>(getHeaders())
                val url = "${audience}users/$userId?fields=name,picture&include_fields=true"
                rest.exchange(url, HttpMethod.GET, request, UserProfileDTO::class.java)
            } catch (e: Exception) {
                throw exceptionsFactory.createBadRequestException("Failed to get user: ${e.message}")
            }

        fun requestNewManagementToken(): String {
            val headers =
                HttpHeaders().apply {
                    contentType = MediaType.APPLICATION_JSON
                    set("User-Agent", "IntelliJ HTTP Client/IntelliJ IDEA 2024.1.6")
                    set("Accept", "*/*")
                }

            val body =
                TokenRequest(
                    client_id = clientId,
                    client_secret = clientSecret,
                    audience = audience,
                    grant_type = "client_credentials",
                )
            val request = HttpEntity(body, headers)

            val response =
                try {
                    rest.exchange(
                        "${auth0Url}oauth/token",
                        HttpMethod.POST,
                        request,
                        object : ParameterizedTypeReference<Map<String, String>>() {},
                    )
                } catch (e: HttpStatusCodeException) {
                    throw exceptionsFactory.createUnauthorizedException(
                        "Failed to get new management token: ${e.statusCode} ${e.responseBodyAsString}",
                    )
                } catch (e: RestClientException) {
                    throw exceptionsFactory.createUnauthorizedException("Failed to get new management token: ${e.message}")
                }

            return response.body?.get("access_token")
                ?: throw exceptionsFactory.createNotFoundException("No access token in response")
        }

        private fun getHeaders(): HttpHeaders {
            if (isTokenExpired(managementToken)) {
                managementToken = requestNewManagementToken()
                updateManagementTokenEnv(managementToken)
            }
            return HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
                set("Authorization", "Bearer $managementToken")
            }
        }

        private fun isTokenExpired(token: String): Boolean {
            val parts = token.split(".")
            if (parts.size != 3) return true

            val payload = String(Base64.getDecoder().decode(parts[1]))
            val mapper = jacksonObjectMapper()

            val jsonObject: Map<String, Any> =
                mapper.readValue(
                    payload,
                    object : TypeReference<Map<String, Any>>() {},
                )

            val exp = (jsonObject["exp"] as Number).toLong()
            val now = System.currentTimeMillis() / 1000

            return now >= exp
        }

        private fun updateManagementTokenEnv(value: String) {
            val key = "AUTH0_MANAGEMENT_TOKEN"
            if (envFilePath.isEmpty() || envFilePath.isBlank()) {
                System.setProperty(key, value)
            } else {
                val envFile = File(envFilePath)
                val lines = envFile.readLines().toMutableList()
                val index = lines.indexOfFirst { it.startsWith("$key=") }
                if (index != -1) {
                    lines[index] = "$key=$value"
                } else {
                    lines.add("$key=$value")
                }
                envFile.writeText(lines.joinToString("\n"))
            }
        }
    }
