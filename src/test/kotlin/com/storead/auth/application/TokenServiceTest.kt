package com.storead.auth.application

import com.storead.IntegrationTestSupport
import com.storead.auth.application.request.TokenServiceRequest
import com.storead.auth.domain.*
import com.storead.profile.domain.ProfileRepository
import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@DisplayName("인증 토큰 서비스 테스트")
class TokenServiceTest(
    @Autowired private val tokenService: TokenService,
    @Autowired private val refreshTokenRepository: RefreshTokenRepository,
    @Autowired private val authRepository: AuthRepository,
    @Autowired private val profileRepository: ProfileRepository,
) : IntegrationTestSupport({

    lateinit var testUser: User

    beforeSpec {
        val user = User(
            name = "testUser",
            email = "testUser@test.com",
            platformId = "1",
            platform = PlatformType.KAKAO
        )

        testUser = authRepository.save(user)
    }

    afterSpec {
        refreshTokenRepository.deleteAll()

        // NOTE: 프로필 -> 유저 단방향 매핑 제약 조건으로 무효화를 위해 프로필 우선 삭제
        profileRepository.deleteAll()
        authRepository.deleteAll()
    }

    given("액세스 토큰 생성") {
        `when`("사용자 정보를 기준으로 액세스 토큰을 발급 하면") {
            val accessToken: String = tokenService.createAccessToken(testUser)

            then("생성된 토큰에는 사용자의 고유 아이디가 포함되어 있어야 한다") {
                tokenService.getSubject(accessToken) shouldBe testUser.id
            }
        }
    }

    given("리프레시 토큰 생성") {
        `when`("사용자 정보를 기준으로 리프레시 토큰을 발급하면") {
            val refreshToken: String = tokenService.createRefreshToken(testUser)

            then("발급된 리프레시 토큰이 레디스에 저장되어야 한다") {
                refreshTokenRepository.findByUserId(testUser.id!!) shouldBe RefreshToken(testUser.id, refreshToken)
            }
        }
    }

    given("액세스 토큰 재발급") {
        val expired = Date(System.currentTimeMillis() - 60 * 1000)
        val expiredAccessToken: String = tokenService.createAccessToken(testUser, expired = expired)

        val tokenRequest = TokenServiceRequest(
            expiredAccessToken,
            tokenService.createRefreshToken(testUser)
        )
        `when`("액세스 토큰과 리프레시 토큰을 입력하면") {
            val tokenResponse = tokenService.reIssue(tokenRequest)

            then("새로운 액세스 토큰을 발급한다") {
                expiredAccessToken shouldNotBe tokenResponse.accessToken
            }
        }
    }

    given("리프레시 토큰 삭제") {
        val accessToken = tokenService.createAccessToken(testUser)
        tokenService.createRefreshToken(testUser)
        `when`("액세스 토큰을 입력 하면") {
            tokenService.delete(accessToken)
            then("해당 유저의 아이디 값으로 저장 되어 있는 리프레시 토큰을 제거한다") {
                refreshTokenRepository.findByUserId(testUser.id!!) shouldBe null
            }
        }
    }
})