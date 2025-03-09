package com.storead.profile.web

import com.storead.auth.domain.User
import com.storead.common.web.ApiResponse
import com.storead.profile.application.ProfileService
import com.storead.profile.web.request.ProfileUpdateRequest
import com.storead.profile.web.response.ProfileResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/profiles")
class ProfileController(
    val profileService: ProfileService
) {

    @GetMapping("/{profileId}")
    fun profile(@PathVariable("profileId") profileId: Long): ResponseEntity<ApiResponse<ProfileResponse>> {
        val response = profileService.getProfileByProfileId(profileId)
        return ApiResponse.success(
            ProfileResponse(response),
            message = "Successfully retrieved profile"
        )
    }

    @GetMapping("/me")
    fun myProfile(@AuthenticationPrincipal user: User): ResponseEntity<ApiResponse<ProfileResponse>> {
        val response = profileService.getProfileByUserId(user.id!!)

        return ApiResponse.success(
            ProfileResponse(response),
            message = "Successfully retrieved profile"
        )
    }

    @PatchMapping("/me/update")
    fun updateMyProfile(@AuthenticationPrincipal user: User, @ModelAttribute request: ProfileUpdateRequest): ResponseEntity<ApiResponse<ProfileResponse>> {
        val serviceResponse = profileService.updateProfile(request.toServiceRequest(user.id!!))

        return ApiResponse.success(
            ProfileResponse(serviceResponse),
            message = "Successfully updated profile",
        )
    }
}