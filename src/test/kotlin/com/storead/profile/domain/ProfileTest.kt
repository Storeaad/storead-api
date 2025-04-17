package com.storead.profile.domain

import com.github.f4b6a3.ulid.UlidCreator
import com.storead.auth.domain.PlatformType
import com.storead.auth.domain.User
import com.storead.profile.application.request.ProfileServiceUpdateRequest
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("프로필 도메인 기능 테스트")
class ProfileTest(
    @Autowired private val profileRepository: ProfileRepository,
) : BehaviorSpec({

    beforeSpec {
        profileRepository.deleteAll()
    }

    given("유저 정보로 프로필을 생성한 상태에서") {
        val testUser = User(
            name = "test",
            platformId = "1",
            platform = PlatformType.KAKAO,
        ).toProfile()

        `when`("프로필 이미지 URL을 설정하면") {
            val testProfile = testUser.uploadProfileImage("test")

            then("프로필에 이미지가 성공적으로 등록되어야 한다") {
                testProfile.image!!.url shouldBe "test"
            }
        }
    }

    given("이미 등록된 프로필이 있는 상태에서") {
        val user = User(platform = PlatformType.KAKAO, platformId = "1", name = "testUser")

        val profile = Profile(
            profileName = "test",
            image = ProfileImage(url = "testProfileImage"),
            user = user,
        )
        val userId = UlidCreator.getMonotonicUlid().toUuid()

        `when`("프로필 이름을 새로운 값으로 업데이트하면") {
            val updateProfile = profile.update(
                ProfileServiceUpdateRequest(userId, "updateProfileName"),
                profileImage = null
            )

            then("프로필 이름이 새 값으로 변경되어야 한다") {
                updateProfile.profileName shouldBe "updateProfileName"
            }
        }

        `when`("새로운 프로필 소개글을 입력하면") {
            val updateProfile = profile.update(
                ProfileServiceUpdateRequest(userId, aboutMe = "updateAboutMe"),
                profileImage = null
            )

            then("프로필 소개글이 새 값으로 변경되어야 한다") {
                updateProfile.aboutMe shouldBe "updateAboutMe"
            }
        }

        `when`("새로운 프로필 이미지를 입력하면") {
            val updateProfile = profile.update(
                ProfileServiceUpdateRequest(userId),
                profileImage = ProfileImage(url = "updateTestImage")
            )

            then("프로필 이미지가 새 이미지로 변경되어야 한다") {
                updateProfile.image!!.url shouldBe "updateTestImage"
            }
        }
    }
})