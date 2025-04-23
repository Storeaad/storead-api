package com.storead.profile.application

import com.storead.profile.application.request.FollowRelationshipServiceRequest
import com.storead.profile.application.request.FollowServiceRequest
import com.storead.profile.application.response.FollowRelationshipResponse
import com.storead.profile.application.response.FollowServiceResponse
import com.storead.profile.application.response.UnfollowServiceResponse
import com.storead.profile.domain.*
import com.storead.profile.exception.FollowException
import com.storead.profile.exception.ProfileException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull


@Service
class FollowService(
    private val followRepository: FollowRepository,
    private val profileRepository: ProfileRepository,
) {

    fun getFollowing(following: FollowRelationshipServiceRequest): FollowRelationshipResponse {

        val followingQueryResult = Relationship(
            followRepository.findFollowingByFromId(
                profileId = following.from,
                limit = following.limit + 1,
                cursor = following.cursor
            ).mapNotNull { follow ->
                profileRepository.findById(follow.toId).getOrNull()?.let { profile ->
                    FollowingProfile(profile, follow.id)
                }
            }
        )


        return followingQueryResult.toFollowRelationshipResponseByFollowing(following)
    }

    fun getFollowers(followerRequest: FollowRelationshipServiceRequest): FollowRelationshipResponse {

        val followersQueryResult = Relationship(
            followRepository.findFollowersByToId(
                profileId = followerRequest.from,
                limit = followerRequest.limit + 1,
                cursor = followerRequest.cursor
            ).mapNotNull { follow ->
                profileRepository.findById(follow.fromId).getOrNull()?.let { profile ->
                    FollowerProfile(profile, follow.id)
                }
            }
        )

        return followersQueryResult.toFollowRelationshipResponseByFollowers(followerRequest)
    }

    @Transactional
    fun follow(followRequest: FollowServiceRequest): FollowServiceResponse {
        if (followRequest.isSelfFollow()) {
            throw FollowException("자기 자신을 팔로우할 수 없습니다.")
        }

        if (followRepository.existsByFromIdAndToId(followRequest.from, followRequest.to)) {
            throw FollowException("이미 팔로우중인 사용자입니다.")
        }

        val from: Profile =
            profileRepository.findById(followRequest.from).orElseThrow { ProfileException("해당 유저의 프로필을 찾을 수 없습니다.") }

        val to: Profile =
            profileRepository.findById(followRequest.to).orElseThrow { ProfileException("해당 프로필을 찾을 수 없습니다.") }

        val follow: Follow = followRepository.save(Follow(fromId = from.id, toId = to.id))
        return FollowServiceResponse(follow.id, from, to)
    }

    @Transactional
    fun unfollow(followRequest: FollowServiceRequest): UnfollowServiceResponse {
        if (followRequest.isSelfFollow()) {
            throw FollowException("자기 자신은 팔로우를 취소할 수 없습니다.")
        }

        val follow = followRepository.findByFromIdAndToId(followRequest.from, followRequest.to)
            ?: throw FollowException("팔로우 정보를 찾을 수 없습니다.")

        followRepository.delete(follow)

        val from: Profile =
            profileRepository.findById(followRequest.from).orElseThrow { ProfileException("해당 유저의 프로필을 찾을 수 없습니다.") }

        val to: Profile =
            profileRepository.findById(followRequest.to).orElseThrow { ProfileException("해당 프로필을 찾을 수 없습니다.") }


        return UnfollowServiceResponse(from, to)
    }

}