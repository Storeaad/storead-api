package com.storead.auth.client.auth

import com.storead.auth.application.request.AuthServiceRequest
import com.storead.auth.client.auth.dto.GoogleAccountResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class Google(
    private val client: RestClient
) : SocialClient {

    override fun getPlatformUserInfoByAccessToken(accessToken: String): AuthServiceRequest {
        val uri = "https://www.googleapis.com/oauth2/v1/tokeninfo"

        return client.post()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .body(GoogleAccountResponse::class.java)!!
            .toServiceRequest()

    }
}