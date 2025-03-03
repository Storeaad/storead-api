package com.storead.auth.client.auth

import com.storead.auth.application.request.AuthServiceRequest
import com.storead.auth.client.auth.dto.KakaoUserResponse
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
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .onStatus({
                it.is4xxClientError || it.is5xxServerError
            }) { _, response ->
                val responseBody = String(response.body.readAllBytes(), Charsets.UTF_8)
                throw IllegalArgumentException("카카오 API 오류: ${response.statusCode} $responseBody")
            }
            .toEntity(KakaoUserResponse::class.java)
            .body!!
            .toServiceRequest()
    }

}
