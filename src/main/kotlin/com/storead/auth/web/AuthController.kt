package com.storead.auth.web

import com.storead.auth.application.AuthService
import com.storead.auth.application.request.AuthServiceRequest
import com.storead.auth.application.response.AuthServiceResponse
import com.storead.auth.client.auth.KakaoClient
import com.storead.auth.web.cookie.Cookie
import com.storead.auth.web.response.LoginResponse
import com.storead.common.web.ApiResponse
import com.storead.config.properties.CookieProperties
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val kakaoClient: KakaoClient,
    private val authService: AuthService,
    private val cookieProperties: CookieProperties,
) {

    @GetMapping("/kakao")
    fun kakaoLogin(@RequestHeader("Authorization") token: String): ResponseEntity<ApiResponse<LoginResponse>> {

        val accessTokenOnly: String = token.removePrefix("Bearer ")
        val serviceRequest: AuthServiceRequest = kakaoClient.getPlatformUserInfoByAccessToken(accessTokenOnly)
        val serviceResponse: AuthServiceResponse = authService.login(serviceRequest)


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

        return ApiResponse.success(LoginResponse(serviceResponse.nickname), cookies)
    }

    @GetMapping("/logout")
    fun logout(@RequestHeader("Authorization") token: String): ResponseEntity<ApiResponse<String>> {
        val cookies: List<ResponseCookie> = listOf(
            Cookie(cookieProperties.accessTokenName, null, 0).create(),
            Cookie(cookieProperties.refreshTokenName, null, 0).create()
        )
        authService.logout(token)
        return ApiResponse.success("Successfully logged out", cookies)
    }
}