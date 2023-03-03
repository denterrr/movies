package ter.den.feature_auth.data.network.model

data class RequestTokenDto(
    val expires_at: String,
    val request_token: String,
    val success: Boolean
)