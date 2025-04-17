package com.storead.auth.domain

import com.github.f4b6a3.ulid.UlidCreator
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("리프레시 토큰 레포지토리 도메인 테스트")
class RefreshTokenRepositoryTest(
    @Autowired private val tokenRepository: RefreshTokenRepository
) : BehaviorSpec({

    given("리프레시 토큰 저장소에서 사용자 ID로 조회할 때") {
        val userId: UUID = UlidCreator.getMonotonicUlid().toUuid()
        val expectedRefreshToken = RefreshToken(userId, "testRefreshToken")
        tokenRepository.save(expectedRefreshToken)

        `when`("존재하는 사용자 ID로 조회하는 경우") {
            val actualRefreshToken: RefreshToken? = tokenRepository.findByUserId(userId)

            then("저장된 리프레시 토큰 정보가 정확히 반환되어야 한다") {
                actualRefreshToken shouldBe expectedRefreshToken
            }
        }

        `when`("존재하지 않는 사용자 ID로 조회하는 경우") {
            val nonExistentUserId: UUID = UlidCreator.getMonotonicUlid().toUuid()
            val actualRefreshToken: RefreshToken? = tokenRepository.findByUserId(nonExistentUserId)

            then("null 값이 반환되어야 한다") {
                actualRefreshToken shouldBe null
            }
        }
    }
})
