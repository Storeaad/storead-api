package com.storead.auth.client.auth

import com.storead.auth.application.request.AuthServiceRequest
import com.storead.auth.client.auth.dto.GithubAccountResponse
import com.storead.auth.exception.AuthException
import com.storead.common.constants.Headers
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class Github(
    private val client: RestClient,
) : SocialClient {

    override fun getPlatformUserInfoByAccessToken(accessToken: String): AuthServiceRequest {
        val uri = "https://api.github.com/user"

        return client.get()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .header(Headers.AUTHORIZATION, "${Headers.BEARER_NEXT_SPACE}$accessToken")
            .retrieve()
            .onStatus({
                it.is4xxClientError || it.is5xxServerError
            }) { _, response ->
                val responseBody = String(response.body.readAllBytes(), Charsets.UTF_8)
                throw AuthException("깃허브 API 오류: ${response.statusCode} $responseBody")
            }
            .body(GithubAccountResponse::class.java)!!
            .toServiceRequest()
    }
}