package com.storead.profile.domain

import com.storead.profile.application.request.FollowRelationshipServiceRequest
import com.storead.profile.application.response.FollowRelationshipResponse
import com.storead.profile.application.response.ProfileServiceResponse

data class Relationship(
    private val follows: List<Follow>,
) {
    companion object {
        fun from(follows: List<Follow>): Relationship {
            if (follows.isEmpty()) {
                throw throw FollowException("해당 유저를 찾지 못하였습니다.")
            }
            return Relationship(follows)
        }
    }

    fun toFollowRelationshipResponseByFollowing(request: FollowingRequest): FollowRelationshipResponse {
        val following = follows.map { ProfileServiceResponse(it.from) }
    fun toFollowRelationshipResponseByFollowing(request: FollowRelationshipServiceRequest): FollowRelationshipResponse {
        val cursor = getCursor(request)

        return FollowRelationshipResponse(following, cursor)
    }

    fun toFollowRelationshipResponseByFollowers(request: FollowingRequest): FollowRelationshipResponse {
        val followers = follows.map { ProfileServiceResponse(it.to) }
    fun toFollowRelationshipResponseByFollowers(request: FollowRelationshipServiceRequest): FollowRelationshipResponse {
        val cursor = getCursor(request)

        return FollowRelationshipResponse(followers, cursor)
    }


    private fun getCursor(
        request: FollowRelationshipServiceRequest,
    ) = if (hasNext(request)) follows.last().id.toString() else null

    private fun hasNext(request: FollowRelationshipServiceRequest) = follows.size > request.limit

}
