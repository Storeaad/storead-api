package com.storead.auth.web

import com.storead.auth.application.TokenService
import com.storead.auth.application.request.TokenServiceRequest
import com.storead.auth.application.response.TokenServiceResponse
import com.storead.auth.web.cookie.Cookie
import com.storead.common.web.ApiResponse
import com.storead.config.properties.CookieProperties
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/token")
class TokenController(
    private val tokenService: TokenService,
    private val cookieProperties: CookieProperties
) {

    @GetMapping("/refresh")
    fun refreshToken(
        @RequestHeader("Authorization") accessToken: String,
        @CookieValue("refreshToken") refreshToken: String,
    ): ResponseEntity<ApiResponse<String>> {
        val accessTokenOnly: String = accessToken.removePrefix("Bearer ")
        val serviceResponse: TokenServiceResponse =
            tokenService.reIssue(TokenServiceRequest(accessTokenOnly, refreshToken))

        val cookies: List<ResponseCookie> = listOf(
            Cookie(
                cookieProperties.accessTokenName,
                serviceResponse.accessToken,
                cookieProperties.accessTokenMaxAge
            ).create(),

            Cookie(
                cookieProperties.refreshTokenName,
                serviceResponse.refreshToken,
                cookieProperties.refreshTokenMaxAge,
                httpOnly = true,
                secure = true
            ).create()
        )

        return ApiResponse.success("successfully refreshed token", cookies)
    }
}