package com.storead.api.auth.controller.dto.request

import jakarta.validation.constraints.NotNull


data class SocialOauthRequest(

    @field:NotNull(message = "플랫폼 서버의 인증된 토큰은 필수입니다.")
    val accessToken: String
)
