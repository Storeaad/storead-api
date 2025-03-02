package com.storead.auth.web.cookie

import org.springframework.http.ResponseCookie

data class Cookie(
    val name: String,
    val value: String?,
    val maxAge: Long,
    val httpOnly: Boolean = false,
    val secure: Boolean = false,
) {
    fun create(): ResponseCookie {
        return ResponseCookie.from(name, value ?: "")
            .httpOnly(httpOnly)
            .secure(secure)
            .path("/")
            .maxAge(maxAge)
            .build()
    }
}
