package com.storead.api.auth.controller

import com.storead.api.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    @GetMapping("/kakao")
    fun kakaoLogin(): ApiResponse<String> {
        return ApiResponse.success("success")
    }
}