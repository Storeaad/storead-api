package com.storead.auth.client.auth

import com.storead.auth.exception.AuthException
import org.springframework.stereotype.Component

@Component
data class Clients(
    val kakao: Kakao,
    val google: Google,
    val github: Github,
) {
    private val factoryMap: Map<String, SocialClient> = mapOf(
        "kakao" to kakao,
        "google" to google,
        "github" to github,
    )

    fun getClientByPlatform(platform: String): SocialClient {
        return factoryMap[platform.lowercase()] ?: throw AuthException("지원하지 않는 소셜 플랫폼입니다.")
    }
}