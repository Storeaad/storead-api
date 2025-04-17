package com.storead.profile.web.request

import com.storead.profile.application.request.FollowRelationshipServiceRequest
import java.util.UUID

data class FollowingRequest(
    val fromProfileId: UUID,
    val cursor: UUID? = null,
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
