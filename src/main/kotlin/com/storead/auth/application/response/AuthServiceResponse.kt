package com.storead.auth.application.response

import com.storead.auth.domain.User

data class AuthServiceResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: User,
)
