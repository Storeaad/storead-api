package com.storead.profile.domain

import com.github.f4b6a3.ulid.UlidCreator
import com.storead.profile.application.response.FollowRelationshipResponse
import com.storead.profile.web.request.FollowingRequest
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@SpringBootTest
@ActiveProfiles("test")
@DisplayName("팔로우 관계 도메인 테스트")
class RelationshipTest(
    @Autowired private val profileRepository: ProfileRepository,
) : BehaviorSpec({

    afterSpec {
        profileRepository.deleteAll()
    }

    given("1번 유저가 2번 유저를 팔로잉 중인 경우") {

        val testProfile1 =
            Profile(profileName = "test1", userId = UlidCreator.getMonotonicUlid().toUuid())
        val testProfile2 =
            Profile(profileName = "test2", userId = UlidCreator.getMonotonicUlid().toUuid())

        val followFromTo = Follow(
            fromId = testProfile1.id,
            toId = testProfile2.id
        )
        val profileWithFollowId = FollowingProfile(testProfile2, followFromTo.id) // toProfile

        val request = FollowingRequest(testProfile1.id).toFollowRelationshipServiceRequest()
        val follows = Relationship(listOf(profileWithFollowId))
        `when`("1번 유저가 팔로우 중인 사용자를 조회 하면") {
            val response: FollowRelationshipResponse = follows.toFollowRelationshipResponseByFollowing(request)
            then("2번 유저가 반환되어야 한다") {
                response.following.map { it.name }.first() shouldBe "test2"
            }
        }
    }

    given("2번 유저가 1번 유저를 팔로잉 중인 경우") {
        val testProfile1 =
            Profile(profileName = "test1", userId = UlidCreator.getMonotonicUlid().toUuid())
        val testProfile2 =
            Profile(profileName = "test2", userId = UlidCreator.getMonotonicUlid().toUuid())

        val followFromTo = Follow(
            fromId = testProfile2.id,
            toId = testProfile1.id
        )

        val profileWithFollowId = FollowerProfile(testProfile2, followFromTo.id) // fromProfile

        val request = FollowingRequest(testProfile2.id).toFollowRelationshipServiceRequest()
        val follows: Relationship = Relationship(listOf(profileWithFollowId))
        `when`("1번 유저를 팔로우 중인 사용자를 조회 하면") {
            val response: FollowRelationshipResponse = follows.toFollowRelationshipResponseByFollowers(request)
            then("2번 유저가 반환된다") {
                response.following.map { it.name }.first() shouldBe "test2"
            }
        }
    }

})