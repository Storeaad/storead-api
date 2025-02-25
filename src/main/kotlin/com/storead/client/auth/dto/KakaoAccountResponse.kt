package com.storead.client.auth.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoUserResponse(
    val id: String,
    val kakaoAccount: KakaoAccount,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoAccount(
    val profile: KakaoProfile,
    val email: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoProfile(
    val nickname: String
)