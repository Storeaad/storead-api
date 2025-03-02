package com.storead.auth.application.response

data class AuthServiceResponse(
    val accessToken: String,
    val refreshToken: String,
    val nickname: String,
)
