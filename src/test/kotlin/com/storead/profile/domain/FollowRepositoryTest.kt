package com.storead.profile.domain

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode.Root
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("팔로우 레포지토리 테스트")
@Transactional
class FollowRepositoryTest(
    @Autowired private val followRepository: FollowRepository,
    @Autowired private val profileRepository: ProfileRepository
) : BehaviorSpec({

    /**
     * 트랜잭션 라이프 사이클 범위 변경
     * - 자료: https://kth990303.tistory.com/374
     */
    extensions(SpringTestExtension(Root))

    afterSpec {
        followRepository.deleteAll()
        profileRepository.deleteAll()
    }


    given("1번 유저가 2번 유저를 팔로우 하고 있는 경우") {
        val testProfile1 = profileRepository.saveAndFlush(Profile(profileName = "test1"))
        val testProfile2 = profileRepository.saveAndFlush(Profile(profileName = "test2"))

        val followFromTo = Follow(
            from = testProfile1,
            to = testProfile2
        )
        followRepository.save(followFromTo)

        `when`("두 아이디를 기준으로 팔로우 정보를 조회 하면") {
            val follow = followRepository.findByFromIdAndToId(testProfile1.id!!, testProfile2.id!!)!!
            then("현재 팔로우 하고 있는 유저를 반환한다") {
                follow
                    .to.shouldBe(testProfile2)
                    .profileName.shouldBe("test2")
            }
        }
    }

    given("2명의 사용자가 1번 유저를 팔로우 하고 있고, 조회 제한이 2로 설정된 경우") {
        val limit = 2

        val testProfile1 = profileRepository.saveAndFlush(Profile(profileName = "test1"))
        val testProfile2 = profileRepository.saveAndFlush(Profile(profileName = "test2"))
        val testProfile3 = profileRepository.saveAndFlush(Profile(profileName = "test3"))

        followRepository.save(
            Follow(
                from = testProfile2,
                to = testProfile1
            )
        )
        followRepository.save(
            Follow(
                from = testProfile3,
                to = testProfile1
            )
        )

        `when`("해당 사용자의 팔로워 목록을 제한(2)개로 조회하면") {
            val followers: List<Follow> = followRepository.findFollowersByToId(testProfile1.id!!, limit = limit)
            then("팔로워 목록을 최신 순서대로 모두 반환한다") {
                followers.map { it.from } shouldContain testProfile3 shouldHaveSize 2
            }
        }
    }

    given("3명의 사용자가 1번 유저를 팔로우 하고 있고, 조회 제한이 2로 설정된 경우") {
        val limit = 2

        val testProfile1 = profileRepository.saveAndFlush(Profile(profileName = "test1"))
        val testProfile2 = profileRepository.saveAndFlush(Profile(profileName = "test2"))
        val testProfile3 = profileRepository.saveAndFlush(Profile(profileName = "test3"))
        val testProfile4 = profileRepository.saveAndFlush(Profile(profileName = "test4"))

        followRepository.save(
            Follow(
                from = testProfile2,
                to = testProfile1
            )
        )
        followRepository.save(
            Follow(
                from = testProfile3,
                to = testProfile1
            )
        )
        followRepository.save(
            Follow(
                from = testProfile4,
                to = testProfile1
            )
        )

        `when`("해당 사용자의 팔로워 목록을 제한(2)개로 조회하면") {
            val followers: List<Follow> = followRepository.findFollowersByToId(testProfile1.id!!, limit = limit)
            then("최신 팔로워 순서대로 2명의 팔로워만 반환된다") {
                followers.map { it.from } shouldNotContain testProfile1 shouldHaveSize 2
            }
        }
    }

    given("1번 유저가 2명의 유저를 팔로우 하고 있고, 조회 제한이 2로 설정된 경우") {
        val limit = 2

        val testProfile1 = profileRepository.saveAndFlush(Profile(profileName = "test1"))
        val testProfile2 = profileRepository.saveAndFlush(Profile(profileName = "test2"))
        val testProfile3 = profileRepository.saveAndFlush(Profile(profileName = "test3"))

        followRepository.save(
            Follow(
                from = testProfile1,
                to = testProfile2
            )
        )
        followRepository.save(
            Follow(
                from = testProfile1,
                to = testProfile3
            )
        )

        `when`("해당 사용자의 팔로잉 목록을 제한(2)개로 조회하면") {
            val following: List<Follow> = followRepository.findFollowingByFromId(testProfile1.id!!, limit = limit)
            then("팔로잉 목록을 최신 순서대로 모두 반환한다") {
                following.map { it.to } shouldContain testProfile2 shouldContain testProfile3 shouldHaveSize 2
            }
        }
    }

    given("1번 유저가 3명의 유저를 팔로우 하고 있고, 조회 제한이 2로 설정된 경우") {
        val limit = 2

        val testProfile1 = profileRepository.saveAndFlush(Profile(profileName = "test1"))
        val testProfile2 = profileRepository.saveAndFlush(Profile(profileName = "test2"))
        val testProfile3 = profileRepository.saveAndFlush(Profile(profileName = "test3"))
        val testProfile4 = profileRepository.saveAndFlush(Profile(profileName = "test4"))

        followRepository.save(
            Follow(
                from = testProfile1,
                to = testProfile2
            )
        )
        followRepository.save(
            Follow(
                from = testProfile1,
                to = testProfile3
            )
        )
        followRepository.save(
            Follow(
                from = testProfile1,
                to = testProfile4
            )
        )

        `when`("해당 사용자의 팔로워 목록을 제한(2)개로 조회하면") {
            val followers: List<Follow> =
                followRepository.findFollowingByFromId(testProfile1.id!!, limit = limit)
            then("최신 팔로워 순서대로 2명의 팔로워만 반환된다") {
                followers.map { it.to } shouldNotContain testProfile2 shouldHaveSize 2
            }
        }
    }
})