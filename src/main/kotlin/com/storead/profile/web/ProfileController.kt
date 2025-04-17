package com.storead.profile.web

import com.storead.auth.domain.User
import com.storead.common.web.ApiResponse
import com.storead.profile.application.ProfileService
import com.storead.profile.web.request.ProfileUpdateRequest
import com.storead.profile.web.response.ProfileResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/v1/profiles")
class ProfileController(
    val profileService: ProfileService
) {

    /**
     * 다른 사용자의 프로필 접근
     *
     * @param profileId 조회할 프로필 ID
     * @return 프로필 정보를 담은 응답 객체
     */
    @GetMapping("/{profileId}")
    fun toProfile(@PathVariable("profileId") profileId: UUID): ResponseEntity<ApiResponse<ProfileResponse>> {
        val response = profileService.getProfileByProfileId(profileId)
        return ApiResponse.success(
            ProfileResponse(response),
            message = "Successfully retrieved profile"
        )
    }

    /**
     * 나의 프로필 접근
     *
     * 인증된 사용자의 프로필 정보를 반환
     * 요청 시 Authorization 헤더에 Bearer 토큰을 포함해야 함.
     *
     * @param user 인증된 사용자 정보 (JWT 토큰에서 추출)
     * @return 프로필 정보를 담은 응답 객체
     */
    @GetMapping("/me")
    fun myProfile(@AuthenticationPrincipal user: User): ResponseEntity<ApiResponse<ProfileResponse>> {
        val response = profileService.getProfileByUserId(user.id!!)

        return ApiResponse.success(
            ProfileResponse(response),
            message = "Successfully retrieved profile"
        )
    }

    /**
     * 내 프로필 정보 업데이트
     *
     * 인증된 사용자의 프로필 정보를 업데이트.
     * 요청 시 Authorization 헤더에 Bearer 토큰을 포함해야 함.
     *
     * @param user 인증된 사용자 정보 (JWT 토큰에서 추출)
     * @param request 업데이트할 프로필 정보
     * @return 업데이트된 프로필 정보를 담은 응답 객체
     */
    @PatchMapping("/me/update")
    fun updateMyProfile(@AuthenticationPrincipal user: User, @ModelAttribute request: ProfileUpdateRequest): ResponseEntity<ApiResponse<ProfileResponse>> {
        val serviceResponse = profileService.updateProfile(request.toServiceRequest(user.id!!))

        return ApiResponse.success(
            ProfileResponse(serviceResponse),
            message = "Successfully updated profile",
        )
    }
}