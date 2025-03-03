package com.storead.config.properties

import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.security.jwt.token")
data class JwtProperties(
    val secret: String,
    val accessTokenLifetimeMinutes: Long,
    val refreshTokenLifetimeDays: Long,
) {
    val secretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray(Charsets.UTF_8))
    }

}