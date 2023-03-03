package ter.den.feature_auth.data.network.model

data class SessionDto(
    val session_id: String,
    val success: Boolean
)

data class RequestTokenForSessionDto(
    val request_token: String
)

data class RequestLoginAndPasswordDto(
    val username: String,
    val password: String,
    val request_token: String
)