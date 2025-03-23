package com.storead.profile.web.request

import com.storead.profile.application.request.FollowRelationshipServiceRequest

data class FollowingRequest(
    val fromProfileId: Long,
    val cursor: Long? = null,
    val limit: Int = 10,
) {
    fun toFollowRelationshipServiceRequest(): FollowRelationshipServiceRequest {
        return FollowRelationshipServiceRequest(
            fromProfileId,
            limit,
            cursor
        )
    }
}
