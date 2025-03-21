package com.storead.profile.application.response

import com.storead.profile.web.response.FollowingResponse

data class FollowRelationshipResponse(
    val following: List<ProfileServiceResponse>,
    val nextCursor: String?,
) {
    fun toFollowingResponse() = FollowingResponse(this)

}
