package com.storead.auth.client.auth

import com.storead.auth.application.request.AuthServiceRequest
import com.storead.auth.client.auth.dto.GithubAccountResponse
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
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .body(GithubAccountResponse::class.java)!!
            .toServiceRequest()
    }
}