package com.storead.auth.application.request

data class TokenServiceRequest(
    val accessToken: String,
    val refreshToken: String,
)
