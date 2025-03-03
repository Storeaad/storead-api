package com.storead.auth.web

import com.storead.auth.application.AuthService
import com.storead.auth.application.request.AuthServiceRequest
import com.storead.auth.application.response.AuthServiceResponse
import com.storead.auth.client.auth.Clients
import com.storead.auth.web.cookie.Cookie
import com.storead.auth.web.response.LoginResponse
import com.storead.common.web.ApiResponse
import com.storead.config.properties.CookieProperties
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
    private val cookieProperties: CookieProperties,
    private val clients: Clients
) {

    @GetMapping("/{platform}")
    fun login(
        @RequestHeader("Authorization") token: String,
        @PathVariable("platform") type: String
    ): ResponseEntity<ApiResponse<LoginResponse>> {
        val socialClient = clients.getClientByPlatform(type)

        val accessTokenOnly: String = token.removePrefix("Bearer ")

        val serviceRequest: AuthServiceRequest = socialClient.getPlatformUserInfoByAccessToken(accessTokenOnly)
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