package com.storead.auth.application

import com.storead.auth.application.request.AuthServiceRequest
import com.storead.auth.domain.AuthRepository
import com.storead.auth.domain.PlatformType
import com.storead.auth.exception.AuthException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
@DisplayName("사용자 인증 서비스 테스트")
class AuthServiceTest(
    @Autowired private val authService: AuthService,
    @Autowired private val authRepository: AuthRepository,
) : BehaviorSpec({

    beforeSpec {
        authRepository.deleteAll()
    }

    given("사용자가 처음 소셜 로그인을 시도하는 경우") {
        val testAuthRequest = AuthServiceRequest(
            name = "test",
            email = "test@test.com",
            platformId = "1",
            platform = PlatformType.KAKAO,
            profileImageUrl = "default"
        )

        `when`("소셜 계정 정보로 사용자를 조회하면") {
            val expected =
                authRepository.findByPlatformIdAndPlatform(testAuthRequest.platformId, testAuthRequest.platform)
            then("사용자 정보가 존재하지 않아야 한다") {
                expected.shouldBeNull()
            }
        }

        `when`("유저 고유 아이디 값으로 조회하면") {
            then("`유저를 조회할 수 없음` 예외 처리를 반환한다") {
                val exception = shouldThrow<AuthException> {
                    authService.getUserById(1L)
                }
                exception.message shouldBe "유저를 찾을 수 없습니다."
            }
        }

        `when`("소셜 로그인 서비스를 호출하면") {
            authService.login(testAuthRequest)

            then("새로운 사용자 정보가 저장되어야 한다") {
                val expected = authRepository.findByPlatformIdAndPlatform(
                    testAuthRequest.platformId,
                    testAuthRequest.platform
                )!!
                expected.name shouldBe "test"
            }
        }
    }

    given("사용자가 이미 소셜 로그인한 이력이 있는 경우") {
        val testAuthRequest = AuthServiceRequest(
            name = "test",
            email = "test@test.com",
            platformId = "1",
            platform = PlatformType.KAKAO,
            profileImageUrl = "default"
        )

        val loginResponse = authService.login(testAuthRequest)

        `when`("소셜 로그인 서비스를 호출하면") {
            then("기존 사용자 정보를 반환해야 한다") {
                loginResponse.user.name shouldBe "test"
            }
        }

        `when`("유저 고유 아이디 값으로 조회하면") {
            val findUserById = authService.getUserById(loginResponse.user.id!!)
            then("유저 정보를 반환한다") {
                findUserById.name shouldBe "test"
            }
        }
    }
})