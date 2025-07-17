package com.storead.profile.domain

import com.github.f4b6a3.ulid.UlidCreator
import com.storead.IntegrationTestSupport
import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import java.util.*


@DisplayName("프로필 레포지토리 테스트")
class ProfileRepositoryTest(
    @Autowired private val profileRepository: ProfileRepository,
) : IntegrationTestSupport({

    lateinit var userId: UUID

    beforeSpec {
        userId = UlidCreator.getMonotonicUlid().toUuid()
        val profileImage = ProfileImage(url = "test")

        profileRepository.save(
            Profile(
                profileName = "test", image = profileImage, userId = userId
            )
        )
    }

    afterSpec {
        profileRepository.deleteAll()

    }

    given("유저의 프로필 조회") {
        `when`("유저 고유 아이디를 입력하면") {
            val profile = profileRepository.findByUserId(userId)!!
            then("해당 유저의 프로필을 반환한다") {
                profile.profileName shouldBe "test"
            }
        }
    }
})