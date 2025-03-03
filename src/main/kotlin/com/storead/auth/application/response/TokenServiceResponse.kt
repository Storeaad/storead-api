package com.storead.auth.application.response

data class TokenServiceResponse(
    val accessToken: String,
    val refreshToken: String,
)
