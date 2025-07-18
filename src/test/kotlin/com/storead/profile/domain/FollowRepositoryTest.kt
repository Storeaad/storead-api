package com.storead.profile.domain

import com.github.f4b6a3.ulid.UlidCreator
import com.storead.IntegrationTestSupport
import io.kotest.core.annotation.DisplayName
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode.Root
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@DisplayName("팔로우 레포지토리 테스트")
@Transactional
class FollowRepositoryTest(
    @Autowired private val followRepository: FollowRepository,
    @Autowired private val profileRepository: ProfileRepository
) : IntegrationTestSupport({

    /**
     * 트랜잭션 라이프 사이클 범위 변경
     * - 자료: https://kth990303.tistory.com/374
     */
    extensions(SpringTestExtension(Root))

    given("1번 유저가 2번 유저를 팔로우 하고 있는 경우") {
        val testProfile1 = profileRepository.saveAndFlush(
            Profile(
                profileName = "test1", userId = UlidCreator.getMonotonicUlid().toUuid()
            )
        )
        val testProfile2 = profileRepository.saveAndFlush(
            Profile(
                profileName = "test2", userId = UlidCreator.getMonotonicUlid().toUuid()
            )
        )

        val followFromTo = Follow(
            fromId = testProfile1.id,
            toId = testProfile2.id,
        )
        followRepository.save(followFromTo)

        `when`("두 아이디를 기준으로 팔로우 정보를 조회 하면") {
            val follow = followRepository.findByFromIdAndToId(testProfile1.id, testProfile2.id)!!
            then("현재 팔로우 하고 있는 유저를 반환한다") {
                follow.toId.shouldBe(testProfile2.id)
            }
        }
    }

    given("2명의 사용자가 1번 유저를 팔로우 하고 있고, 조회 제한이 2로 설정된 경우") {
        val limit = 2

        val testProfile1 = profileRepository.saveAndFlush(
            Profile(
                profileName = "test1", userId = UlidCreator.getMonotonicUlid().toUuid()
            )
        )
        val testProfile2 = profileRepository.saveAndFlush(
            Profile(
                profileName = "test2", userId = UlidCreator.getMonotonicUlid().toUuid()
            )
        )
        val testProfile3 = profileRepository.saveAndFlush(
            Profile(
                profileName = "test3", userId = UlidCreator.getMonotonicUlid().toUuid()
            )
        )

        followRepository.save(
            Follow(
                fromId = testProfile2.id,
                toId = testProfile1.id
            )
        )
        followRepository.save(
            Follow(
                fromId = testProfile3.id,
                toId = testProfile1.id
            )
        )

        `when`("해당 사용자의 팔로워 목록을 제한(2)개로 조회하면") {
            val followers: List<Follow> = followRepository.findFollowersByToId(testProfile1.id, limit = limit)
            then("팔로워 목록을 최신 순서대로 모두 반환한다") {
                followers.map { it.fromId } shouldContain testProfile3.id shouldHaveSize 2
            }
        }
    }

    given("3명의 사용자가 1번 유저를 팔로우 하고 있고, 조회 제한이 2로 설정된 경우") {
        val limit = 2

        val testProfile1 = profileRepository.saveAndFlush(
            Profile(
                profileName = "test1", userId = UlidCreator.getMonotonicUlid().toUuid()
            )
        )
        val testProfile2 = profileRepository.saveAndFlush(
            Profile(
                profileName = "test2", userId = UlidCreator.getMonotonicUlid().toUuid()
            )
        )
        val testProfile3 = profileRepository.saveAndFlush(
            Profile(
                profileName = "test3", userId = UlidCreator.getMonotonicUlid().toUuid()
            )
        )
        val testProfile4 = profileRepository.saveAndFlush(
            Profile(
                profileName = "test4", userId = UlidCreator.getMonotonicUlid().toUuid()
            )
        )

        followRepository.save(
            Follow(
                fromId = testProfile2.id,
                toId = testProfile1.id
            )
        )
        followRepository.save(
            Follow(
                fromId = testProfile3.id,
                toId = testProfile1.id
            )
        )
        followRepository.save(
            Follow(
                fromId = testProfile4.id,
                toId = testProfile1.id
            )
        )

        `when`("해당 사용자의 팔로워 목록을 제한(2)개로 조회하면") {
            val followers: List<Follow> = followRepository.findFollowersByToId(testProfile1.id, limit = limit)
            then("최신 팔로워 순서대로 2명의 팔로워만 반환된다") {
                followers.map { it.fromId } shouldNotContain testProfile1.id shouldHaveSize 2
            }
        }
    }

    given("1번 유저가 2명의 유저를 팔로우 하고 있고, 조회 제한이 2로 설정된 경우") {
        val limit = 2

        val testProfile1 = profileRepository.saveAndFlush(
            Profile(
                profileName = "test1", userId = UlidCreator.getMonotonicUlid().toUuid()
            )
        )
        val testProfile2 = profileRepository.saveAndFlush(
            Profile(
                profileName = "test2", userId = UlidCreator.getMonotonicUlid().toUuid()
            )
        )
        val testProfile3 = profileRepository.saveAndFlush(
            Profile(profileName = "test3", userId = UlidCreator.getMonotonicUlid().toUuid())
        )

        followRepository.save(
            Follow(
                fromId = testProfile1.id,
                toId = testProfile2.id
            )
        )
        followRepository.save(
            Follow(
                fromId = testProfile1.id,
                toId = testProfile3.id
            )
        )

        `when`("해당 사용자의 팔로잉 목록을 제한(2)개로 조회하면") {
            val following: List<Follow> = followRepository.findFollowingByFromId(testProfile1.id, limit = limit)
            then("팔로잉 목록을 최신 순서대로 모두 반환한다") {
                following.map { it.toId } shouldContain testProfile2.id shouldContain testProfile3.id shouldHaveSize 2
            }
        }
    }

    given("1번 유저가 3명의 유저를 팔로우 하고 있고, 조회 제한이 2로 설정된 경우") {
        val limit = 2

        val testProfile1 =
            profileRepository.saveAndFlush(
                Profile(
                    profileName = "test1",
                    userId = UlidCreator.getMonotonicUlid().toUuid()
                )
            )
        val testProfile2 =
            profileRepository.saveAndFlush(
                Profile(
                    profileName = "test2",
                    userId = UlidCreator.getMonotonicUlid().toUuid()
                )
            )
        val testProfile3 =
            profileRepository.saveAndFlush(
                Profile(
                    profileName = "test3",
                    userId = UlidCreator.getMonotonicUlid().toUuid()
                )
            )
        val testProfile4 =
            profileRepository.saveAndFlush(
                Profile(
                    profileName = "test4",
                    userId = UlidCreator.getMonotonicUlid().toUuid()
                )
            )

        followRepository.save(
            Follow(
                fromId = testProfile1.id,
                toId = testProfile2.id
            )
        )
        followRepository.save(
            Follow(
                fromId = testProfile1.id,
                toId = testProfile3.id
            )
        )
        followRepository.save(
            Follow(
                fromId = testProfile1.id,
                toId = testProfile4.id
            )
        )

        `when`("해당 사용자의 팔로워 목록을 제한(2)개로 조회하면") {
            val followers: List<Follow> =
                followRepository.findFollowingByFromId(testProfile1.id, limit = limit)
            then("최신 팔로워 순서대로 2명의 팔로워만 반환된다") {
                followers.map { it.toId } shouldNotContain testProfile2.id shouldHaveSize 2
            }
        }
    }
})