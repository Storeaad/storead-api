package com.storead.auth.client.auth.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.storead.auth.application.request.AuthServiceRequest
import com.storead.auth.domain.PlatformType


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoUserResponse(
    val id: String,
    val kakaoAccount: KakaoAccount,
) {
    fun toServiceRequest(): AuthServiceRequest {
        return AuthServiceRequest(
            name = kakaoAccount.profile.nickname,
            email = kakaoAccount.email,
            platformId = this.id,
            platform = PlatformType.KAKAO,
        )
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoAccount(
    val profile: KakaoProfile,
    val email: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoProfile(
    val nickname: String
)