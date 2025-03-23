package com.storead.profile.web.response

import com.storead.profile.application.response.FollowRelationshipResponse

data class FollowingResponse(
    private val serviceResponse: FollowRelationshipResponse,
) {
    val nextCursor: String? = serviceResponse.nextCursor
    val profiles: List<ProfileResponse> = serviceResponse.following.stream().map { ProfileResponse(it) }.toList()

}
