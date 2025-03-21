package com.storead.profile.domain

import com.storead.profile.application.response.FollowRelationshipResponse
import com.storead.profile.application.response.ProfileServiceResponse
import com.storead.profile.exception.FollowException
import com.storead.profile.web.request.FollowingRequest

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
        val cursor = getCursor(request)

        return FollowRelationshipResponse(following, cursor)
    }

    fun toFollowRelationshipResponseByFollowers(request: FollowingRequest): FollowRelationshipResponse {
        val followers = follows.map { ProfileServiceResponse(it.to) }
        val cursor = getCursor(request)

        return FollowRelationshipResponse(followers, cursor)
    }


    private fun getCursor(
        request: FollowingRequest,
    ) = if (hasNext(request)) follows[request.limit - 1].id.toString() else null

    private fun hasNext(request: FollowingRequest) = follows.size > request.limit

}
