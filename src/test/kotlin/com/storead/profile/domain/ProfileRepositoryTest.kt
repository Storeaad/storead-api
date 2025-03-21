package com.storead.profile.domain

import com.storead.auth.domain.PlatformType
import com.storead.auth.domain.User
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@SpringBootTest
@ActiveProfiles("test")
@DisplayName("프로필 레포지토리 테스트")
class ProfileRepositoryTest(
    @Autowired private val profileRepository: ProfileRepository,
) : BehaviorSpec({

    lateinit var user: User

    beforeSpec {
        user = User(email = "test@test.com", name = "test", platformId = "1", platform = PlatformType.KAKAO)
        val profileImage = ProfileImage(url = "test")

        profileRepository.save(
            Profile(
                profileName = "test", user = user, image = profileImage
            )
        )
    }

    afterSpec {
        profileRepository.deleteAll()

    }

    given("유저의 프로필 조회") {
        `when`("유저 고유 아이디를 입력하면") {
            val profile = profileRepository.findByUserId(user.id!!)!!
            then("해당 유저의 프로필을 반환한다") {
                profile.profileName.shouldBe("test")
            }
        }
    }

})