package com.storead.profile.web

import com.storead.auth.domain.User
import com.storead.common.web.ApiResponse
import com.storead.profile.application.ProfileService
import com.storead.profile.web.request.ProfileUpdateRequest
import com.storead.profile.web.response.ProfileResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/profiles")
class ProfileController(
    val profileService: ProfileService
) {

    @GetMapping("/me")
    fun myProfile(@AuthenticationPrincipal user: User): ResponseEntity<ApiResponse<ProfileResponse>> {
        val serviceResponse = profileService.getMyProfile(user.id!!)

        return ApiResponse.success(
            ProfileResponse(serviceResponse),
            message = "Successfully retrieved profile"
        )
    }

    @PatchMapping("/me/update")
    fun updateMyProfile(@AuthenticationPrincipal user: User, request: ProfileUpdateRequest): ResponseEntity<ApiResponse<ProfileResponse>> {
        profileService.updateProfile(request.toServiceRequest(user.id!!))

    }
}