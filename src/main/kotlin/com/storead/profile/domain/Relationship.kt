package com.storead.profile.domain

import com.storead.profile.application.request.FollowRelationshipServiceRequest
import com.storead.profile.application.response.FollowRelationshipResponse
import com.storead.profile.application.response.ProfileServiceResponse
import java.util.*


interface FollowRelationView {

    val profile: Profile

    val followId: UUID
}


data class FollowingProfile(
    override val profile: Profile, // NOTE: 내가 팔로우하는 상대방
    override val followId: UUID,
) : FollowRelationView


data class FollowerProfile(
    override val profile: Profile,  // NOTE: 나를 팔로우하는 사람
    override val followId: UUID,
) : FollowRelationView


data class Relationship(
    private val follows: List<FollowRelationView>,
) {
    fun toFollowRelationshipResponseByFollowing(request: FollowRelationshipServiceRequest): FollowRelationshipResponse {
        val following = follows.take(request.limit).map { ProfileServiceResponse(it.profile) }
        val cursor = getCursor(request)

        return FollowRelationshipResponse(following = following, nextCursor = cursor)
    }

    fun toFollowRelationshipResponseByFollowers(request: FollowRelationshipServiceRequest): FollowRelationshipResponse {
        val followers = follows.take(request.limit).map { ProfileServiceResponse(it.profile) }
        val cursor = getCursor(request)

        return FollowRelationshipResponse(following = followers, nextCursor = cursor)
    }


    private fun getCursor(
        request: FollowRelationshipServiceRequest,
    ) = if (hasNext(request)) follows.last().followId.toString() else null

    private fun hasNext(request: FollowRelationshipServiceRequest) = follows.size > request.limit

}
