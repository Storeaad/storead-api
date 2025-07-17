package com.storead.profile.application

import com.storead.IntegrationTestSupport
import com.storead.auth.domain.PlatformType
import com.storead.auth.domain.User
import com.storead.auth.signal.UserCreateEvent
import com.storead.profile.application.request.ProfileServiceUpdateRequest
import com.storead.profile.domain.ProfileRepository
import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import kotlin.io.path.Path


@DisplayName("프로필 서비스 테스트")
class ProfileServiceTest(
    @Autowired private val profileService: ProfileService,
    @Autowired private val profileRepository: ProfileRepository
) : IntegrationTestSupport({

    lateinit var user: User
    lateinit var profileImageFile: MultipartFile

    beforeSpec {
        user = User(platform = PlatformType.KAKAO, platformId = "1", name = "testUser")

        profileImageFile = MockMultipartFile(
            "profileImage",
            "test.png",
            "image/png",
            "test-image-content".toByteArray()
        )
    }

    afterSpec {
        profileRepository.deleteAll()

        val savedFilePath = Path("src/main/resources/static/${profileImageFile.originalFilename}")
        Files.deleteIfExists(savedFilePath)
    }

    given("유저 회원가입 이벤트가 발생한 상황에서") {
        val userCreateEvent = UserCreateEvent(
            user,
            profileImageUrl = "profileImageURL"
        )

        `when`("프로필 서비스가 유저 생성 이벤트를 수신하면") {
            profileService.userCreateEventListen(userCreateEvent)

            then("해당 유저의 프로필이 생성되어야 한다") {
                profileRepository.findByUserId(user.id!!)!!.profileName shouldBe "testUser"
            }
        }
    }

    given("존재하는 유저의 ID로") {
        val userId = user.id!!
        `when`("프로필 서비스에 프로필 조회를 요청하면") {
            val profile = profileService.getProfileByUserId(userId)
            then("해당 유저 ID와 일치하는 프로필이 반환되어야 한다") {
                profile.userId shouldBe userId
            }
        }
    }

    given("사용자가 프로필 업데이트 시 새로운 이미지 파일을 업로드하는 경우") {
        val request = ProfileServiceUpdateRequest(user.id!!, imageFile = profileImageFile)

        `when`("프로필 서비스를 통해 이미지 업데이트를 요청하면") {
            val updateProfile = profileService.updateProfile(request)

            then("응답에 새 이미지 URL이 포함되어야 한다") {
                updateProfile.image.url shouldContain "test.png"
            }
        }
    }
})