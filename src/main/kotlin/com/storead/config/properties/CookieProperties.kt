package com.storead.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.security.jwt.cookie")
data class CookieProperties(
    val accessTokenName: String,
    val refreshTokenName: String,
    val accessTokenMaxAge: Long,
    val refreshTokenMaxAge: Long,
)
