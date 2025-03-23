package com.storead.profile.application

import com.storead.auth.domain.PlatformType
import com.storead.auth.domain.User
import com.storead.profile.application.request.FollowRelationshipServiceRequest
import com.storead.profile.application.request.FollowServiceRequest
import com.storead.profile.domain.FollowRepository
import com.storead.profile.domain.Profile
import com.storead.profile.domain.ProfileRepository
import com.storead.profile.exception.FollowException
import com.storead.profile.exception.ProfileException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode.Root
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional


@SpringBootTest
@ActiveProfiles("test")
@DisplayName("팔로우 서비스 테스트")
@Transactional
class FollowServiceTest(
    @Autowired private val service: FollowService,
    @Autowired private val followRepository: FollowRepository,
    @Autowired private val profileRepository: ProfileRepository,

    ) : BehaviorSpec({

    lateinit var testProfile1: Profile
    lateinit var testProfile2: Profile
    lateinit var testProfile3: Profile
    lateinit var testProfile4: Profile

    extensions(SpringTestExtension(Root))

    beforeSpec {
        testProfile1 = profileRepository.save(
            Profile(
                profileName = "test1",
                user = User(id = null, "1", name = "test1", platform = PlatformType.KAKAO)
            )
        )
        testProfile2 = profileRepository.save(
            Profile(
                profileName = "test2",
                user = User(id = null, "2", name = "test2", platform = PlatformType.KAKAO)
            )
        )
        testProfile3 = profileRepository.save(
            Profile(
                profileName = "test3",
                user = User(id = null, "3", name = "test3", platform = PlatformType.KAKAO)
            )
        )

        testProfile4 = profileRepository.save(
            Profile(
                profileName = "test4",
                user = User(id = null, "4", name = "test4", platform = PlatformType.KAKAO)
            )
        )


    }

    afterSpec {
        followRepository.deleteAll()
        profileRepository.deleteAll()
    }

    given("팔로우 맺지 않은 사용자에게 팔로우 하는 경우") {
        val request = FollowServiceRequest(testProfile1.id!!, testProfile2.id!!)
        `when`("첫 번째 사용자가 두 번째 사용자에게 팔로우를 요청하면") {
            val response = service.follow(request)
            then("정상적으로 팔로우 관계가 생성되어야 한다") {
                with(response.follow) {
                    from.profileName shouldBe "test1"
                    to.profileName shouldBe "test2"
                }
            }
        }
        `when`("이미 팔로우 중인 사용자에게 팔로우를 요청하면") {
            followRepository.deleteAll()
            service.follow(request)
            then("이미 팔로우중인 사용자는 팔로우할 수 없다는 에러가 발생한다") {
                val exception = shouldThrow<FollowException> {
                    service.follow(request)
                }
                exception.message shouldBe "이미 팔로우중인 사용자입니다."
            }
        }

        `when`("자기 자신에게 팔로우를 요청하면") {
            then("자기 자신은 팔로우 할 수 없다는 에러가 발생한다") {
                val exception = shouldThrow<FollowException> {
                    service.follow(FollowServiceRequest(testProfile1.id!!, testProfile1.id!!))
                }
                exception.message shouldBe "자기 자신을 팔로우할 수 없습니다."
            }
        }

        `when`("존재 하지 않는 사용자에게 팔로우를 요청하면") {
            then("유저를 찾을 수 없다는 에러가 발생한다") {
                val exception = shouldThrow<ProfileException> {
                    service.follow(FollowServiceRequest(testProfile1.id!!, 12345))
                }
                exception.message shouldBe "해당 프로필을 찾을 수 없습니다."
            }
        }
    }

    given("팔로우 맺은 사용자를 끊으려는 경우") {
        val request = FollowServiceRequest(testProfile1.id!!, testProfile2.id!!)
        service.follow(request)
        `when`("첫 번째 사용자가 두 번째 사용자에게 팔로우 끊기를 요청하면") {
            service.unfollow(request)
            then("팔로우 관계가 삭제 된다") {
                followRepository.existsByFromIdAndToId(testProfile1.id!!, testProfile2.id!!) shouldBe false
            }
        }

        `when`("자기 자신에게 팔로우 끊기를 요청하면") {
            then("자기 자신은 팔로우를 끊을 수 없다는 에러가 발생한다") {
                val exception = shouldThrow<FollowException> {
                    service.unfollow(FollowServiceRequest(testProfile1.id!!, testProfile1.id!!))
                }
                exception.message shouldBe "자기 자신은 팔로우를 취소할 수 없습니다."
            }
        }

        `when`("존재하지 않는 유저에게 팔로우 끊기를 요청하면") {
            then("팔로우 관계 정보를 찾을 수 없다는 에러가 발생한다") {
                val exception = shouldThrow<FollowException> {
                    service.unfollow(FollowServiceRequest(testProfile1.id!!, 12345))
                }
                exception.message shouldBe "팔로우 정보를 찾을 수 없습니다."
            }
        }
    }

    given("1번 사용자가 2번 사용자를 팔로우 하고 있는 경우") {
        service.follow(FollowServiceRequest(testProfile1.id!!, testProfile2.id!!))
        val request = FollowRelationshipServiceRequest(
            testProfile1.id!!,
            limit = 10,
            cursor = null
        )
        `when`("1번 사용자의 팔로우 정보를 요청하면") {
            val following = service.getFollowing(request)
            then("팔로우 하고 있는 유저들을 반환한다") {
                following.following shouldHaveSize 1
                following.following.map { it.name }.shouldContainExactly("test2")
            }
        }

        `when`("1번 사용자를 팔로우 하고 있는 유저 정보를 요청하면") {
            val followers = service.getFollowers(request)
            then("아무도 팔로우 하고 있지 않기 때문에 비어있다") {
                followers.following.shouldBeEmpty()
            }
        }
    }

    given("1번 사용자가 여러 사용자를 팔로우 하고 있는 경우") {
        val request1 = FollowServiceRequest(testProfile1.id!!, testProfile2.id!!)
        val request2 = FollowServiceRequest(testProfile1.id!!, testProfile3.id!!)
        val request3 = FollowServiceRequest(testProfile1.id!!, testProfile4.id!!)

        val follow1 = service.follow(request1)
        val follow2 = service.follow(request2)
        val follow3 = service.follow(request3)

        val request = FollowRelationshipServiceRequest(
            testProfile1.id!!,
            limit = 2,
            cursor = null
        )

        `when`("1번 사용자의 팔로우 정보를 2명으로 제한 하고 커서 값을 입력하지 않고 요청하면") {
            val following = service.getFollowing(request)
            then("가장 마지막에 팔로우 한 순서대로 2명만 조회 된다") {
                following.following shouldHaveSize 2
                following.following.map { it.name } shouldContainExactly listOf("test4", "test3")
            }

            then("다음 사용자 정보를 조회할 수 있는 커서 정보가 반환 되어야한다") {
                following.toFollowingResponse().nextCursor.shouldNotBeEmpty()
            }
        }

        `when`("1번 사용자의 팔로우 정보를 2명으로 제한 하고 커서 값을 입력하고 요청하면") {
            val request = FollowRelationshipServiceRequest(
                testProfile1.id!!,
                limit = 2,
                cursor = follow2.follow.id // NOTE: 커서 값은 마지막 조회 된 팔로우 정보의 ID 값보다 작은걸 가져온다
            )
            val following = service.getFollowing(request)

            then("입력한 커서 값 부터 팔로우 중인 유저를 반환한다") {
                following.following shouldHaveSize 1
                following.following.map { it.name } shouldContainExactly listOf("test2")
            }
        }
    }

    given("1번 사용자를 여러 사용자가 팔로우 하고 있는 경우") {
        val request1 = FollowServiceRequest(testProfile2.id!!, testProfile1.id!!)
        val request2 = FollowServiceRequest(testProfile3.id!!, testProfile1.id!!)
        val request3 = FollowServiceRequest(testProfile4.id!!, testProfile1.id!!)

        val follow1 = service.follow(request1)
        val follow2 = service.follow(request2)
        val follow3 = service.follow(request3)

        val request = FollowRelationshipServiceRequest(
            testProfile1.id!!,
            limit = 2,
            cursor = null
        )

        `when`("1번 사용자의 팔로워 정보를 2명으로 제한 하고 커서 값을 입력하지 않고 요청하면") {
            val followers = service.getFollowers(request)
            then("가장 마지막에 팔로우 하고 있는 순서대로 2명만 조회 된다") {
                followers.following shouldHaveSize 2
                followers.following.map { it.name } shouldContainExactly listOf("test4", "test3")
            }

            then("다음 사용자 정보를 조회할 수 있는 커서 정보가 반환 되어야한다") {
                followers.toFollowingResponse().nextCursor.shouldNotBeEmpty()
            }
        }

        `when`("1번 사용자의 팔로우 정보를 2명으로 제한 하고 커서 값을 입력하고 요청하면") {
            val request = FollowRelationshipServiceRequest(
                testProfile1.id!!,
                limit = 2,
                cursor = follow2.follow.id
            )
            val followers = service.getFollowers(request)

            then("입력한 커서 값 부터 팔로우 중인 유저를 반환한다") {
                followers.following shouldHaveSize 1
                followers.following.map { it.name } shouldContainExactly listOf("test2")
            }
        }
    }
})
