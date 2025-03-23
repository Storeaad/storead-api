package com.storead.profile.domain

import com.storead.profile.application.request.FollowRelationshipServiceRequest
import com.storead.profile.application.response.FollowRelationshipResponse
import com.storead.profile.application.response.ProfileServiceResponse

data class Relationship(
    private val follows: List<Follow>,
) {
    fun toFollowRelationshipResponseByFollowing(request: FollowRelationshipServiceRequest): FollowRelationshipResponse {
        val following = follows.take(request.limit).map { ProfileServiceResponse(it.to) }
        val cursor = getCursor(request)

        return FollowRelationshipResponse(following, cursor)
    }

    fun toFollowRelationshipResponseByFollowers(request: FollowRelationshipServiceRequest): FollowRelationshipResponse {
        val followers = follows.take(request.limit).map { ProfileServiceResponse(it.from) }
        val cursor = getCursor(request)

        return FollowRelationshipResponse(followers, cursor)
    }


    private fun getCursor(
        request: FollowRelationshipServiceRequest,
    ) = if (hasNext(request)) follows.last().id.toString() else null

    private fun hasNext(request: FollowRelationshipServiceRequest) = follows.size > request.limit

}
