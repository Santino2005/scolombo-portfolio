package tome

data class TokenRequest(
    val client_id: String,
    val client_secret: String,
    val audience: String,
    val grant_type: String,
)
