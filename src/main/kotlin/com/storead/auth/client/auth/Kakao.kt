package com.storead.auth.client.auth

import com.storead.auth.application.request.AuthServiceRequest
import com.storead.auth.client.auth.dto.KakaoUserResponse
import com.storead.auth.exception.AuthException
import com.storead.common.constants.Headers
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class Kakao(private val client: RestClient): SocialClient {

    override fun getPlatformUserInfoByAccessToken(accessToken: String): AuthServiceRequest {
        val uri = "https://kapi.kakao.com/v2/user/me"

        return client.get()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .header(Headers.AUTHORIZATION, "${Headers.BEARER_NEXT_SPACE}$accessToken")
            .retrieve()
            .onStatus({
                it.is4xxClientError || it.is5xxServerError
            }) { _, response ->
                val responseBody = String(response.body.readAllBytes(), Charsets.UTF_8)
                throw AuthException("카카오 API 오류: ${response.statusCode} $responseBody")
            }
            .toEntity(KakaoUserResponse::class.java)
            .body!!
            .toServiceRequest()
    }

}
