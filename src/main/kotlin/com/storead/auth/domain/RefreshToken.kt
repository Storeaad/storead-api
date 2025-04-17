package com.storead.auth.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.util.UUID


@RedisHash(value = "refresh_token", timeToLive = 24 * 60 * 60)
data class RefreshToken(

    @Id
    val userId: UUID? = null,

    private var refreshToken: String
) {
    fun validate(refreshToken: String) =
        this.refreshToken == refreshToken

    fun update(refreshToken: String): RefreshToken {
        this.refreshToken = refreshToken
        return this
    }
}
