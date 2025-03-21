package com.storead.profile.web

import com.storead.common.web.ApiResponse
import com.storead.profile.UserProfile
import com.storead.profile.application.FollowService
import com.storead.profile.application.request.FollowServiceRequest
import com.storead.profile.application.response.FollowRelationshipResponse
import com.storead.profile.web.request.FollowingRequest
import com.storead.profile.web.response.FollowingResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/profiles")
class FollowController(
    private val followService: FollowService,
) {


    /**
     * 다른 사용자 팔로우
     *
     * 현재 로그인한 사용자가 다른 사용자를 팔로우 함.
     * 요청 시 Authorization 헤더에 Bearer 토큰을 포함해야 함.
     *
     * @param toProfileId 팔로우할 대상 사용자의 프로필 ID
     * @param fromProfileId 팔로우를 요청하는 사용자의 프로필 ID (현재 로그인한 사용자)
     * @return 팔로우 요청 결과
     */
    @GetMapping("/{toProfileId}/follow")
    fun follow(
        @UserProfile fromProfileId: Long,
        @PathVariable("toProfileId") toProfileId: Long,
    ): ResponseEntity<ApiResponse<String>> {
        val followResponse = followService.follow(FollowServiceRequest(fromProfileId, toProfileId))

        return ApiResponse.success(
            data = "",
            message = "${followResponse.fromUser}님이 ${followResponse.toUser} 님을 팔로우하기 시작합니다."
        )
    }

    /**
     * 다른 사용자 팔로우 취소
     *
     * 현재 로그인한 사용자가 다른 사용자를 팔로우 취소 함.
     * 요청 시 Authorization 헤더에 Bearer 토큰을 포함해야 함.
     *
     * @param toProfileId 팔로우 취소할 대상 사용자의 프로필 ID
     * @param fromProfileId 팔로우를 취소 하는 사용자의 프로필 ID (현재 로그인한 사용자)
     * @return 팔로우 취소 요청 결과
     */
    @GetMapping("/{toProfileId}/unfollow")
    fun unfollow(
        @UserProfile fromProfileId: Long,
        @PathVariable("toProfileId") toProfileId: Long,
    ): ResponseEntity<ApiResponse<String>> {
        val unfollowResponse = followService.unfollow(FollowServiceRequest(fromProfileId, toProfileId))

        return ApiResponse.success(
            data = "",
            message = "더이상 ${unfollowResponse.toUser} 님을 팔로우 하지 않습니다."
        )
    }


    /**
     * 나의 팔로잉 목록 조회
     *
     * 현재 로그인한 사용자의 팔로잉 목록을 조회 함.
     * 요청 시 Authorization 헤더에 Bearer 토큰을 포함해야 함.
     *
     * @param fromProfileId 팔로잉 목록을 요청하는 사용자의 프로필 ID (현재 로그인한 사용자)
     * @return 팔로잉 목록 조회 결과
     */
    @GetMapping("/me/following")
    fun myFollowing(
        @UserProfile fromProfileId: Long,
        @RequestParam("cursor", required = false) cursor: Long?,
    ): ResponseEntity<ApiResponse<FollowingResponse>> {
        val followResponse: FollowRelationshipResponse =
            followService.getFollowing(FollowingRequest(fromProfileId, cursor))

        return ApiResponse.success(
            data = followResponse.toFollowingResponse(),
            message = "팔로잉 목록을 성공적으로 가져왔습니다."
        )
    }


    /**
     * 나의 팔로워 목록 조회
     *
     * 현재 로그인한 사용자의 팔로워 목록을 조회 함.
     * 요청 시 Authorization 헤더에 Bearer 토큰을 포함해야 함.
     *
     * @param fromProfileId 팔로워 목록을 요청하는 사용자의 프로필 ID (현재 로그인한 사용자)
     * @return 팔로워 목록 조회 결과
     */
    @GetMapping("/me/followers")
    fun myFollowers(
        @UserProfile fromProfileId: Long,
        @RequestParam("cursor", required = false) cursor: Long?,
    ): ResponseEntity<ApiResponse<FollowingResponse>> {
        val followResponse: FollowRelationshipResponse =
            followService.getFollowers(FollowingRequest(fromProfileId, cursor))

        return ApiResponse.success(
            data = followResponse.toFollowingResponse(),
            message = "팔로워 목록을 성공적으로 가져왔습니다."
        )
    }

    /**
     * 다른 유저의 팔로워 목록 조회
     *
     * 입력한 사용자의 팔로워 목록을 조회 함.
     * 요청 시 Authorization 헤더에 Bearer 토큰을 포함해야 함.
     *
     * @param profileId 팔로워 목록을 요청하는 사용자의 프로필 ID
     * @return 팔로워 목록 조회 결과
     */
    @GetMapping("/{profileId}/followers")
    fun userFollowers(
        @PathVariable profileId: Long,
        @RequestParam("cursor", required = false) cursor: Long?,
    ): ResponseEntity<ApiResponse<FollowingResponse>> {
        val followResponse: FollowRelationshipResponse = followService.getFollowers(FollowingRequest(profileId, cursor))

        return ApiResponse.success(
            data = followResponse.toFollowingResponse(),
            message = "팔로워 목록을 성공적으로 가져왔습니다."
        )
    }

    /**
     * 다른 유저의 팔로잉 목록 조회
     *
     * 입력한 사용자의 팔로잉 목록을 조회 함.
     * 요청 시 Authorization 헤더에 Bearer 토큰을 포함해야 함.
     *
     * @param profileId 팔로잉 목록을 요청하는 사용자의 프로필 ID
     * @return 팔로잉 목록 조회 결과
     */
    @GetMapping("/{profileId}/following")
    fun userFollowing(
        @PathVariable profileId: Long,
        @RequestParam("cursor", required = false) cursor: Long?,
    ): ResponseEntity<ApiResponse<FollowingResponse>> {
        val followResponse: FollowRelationshipResponse = followService.getFollowing(FollowingRequest(profileId, cursor))

        return ApiResponse.success(
            data = followResponse.toFollowingResponse(),
            message = "팔로잉 목록을 성공적으로 가져왔습니다."
        )
    }

}