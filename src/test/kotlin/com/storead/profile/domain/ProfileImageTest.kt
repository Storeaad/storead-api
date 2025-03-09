package com.storead.profile.domain

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("프로필 이미지 도메인 테스트")
class ProfileImageTest(
    @Autowired private val profileImageRepository: ProfileImageRepository,
) : BehaviorSpec({

    lateinit var savedProfileImage: ProfileImage

    beforeSpec {
        savedProfileImage = profileImageRepository.saveAndFlush(
            ProfileImage(
                url = "before"
            )
        )

    }

    afterSpec {
        profileImageRepository.deleteAll()
    }

    given("프로필 이미지의 새로운 URL이 주어졌을 때") {
        val newProfileImageUrl = "update"
        `when`("해당 프로필 이미지의 URL을 업데이트 하면") {
            val profileImage = profileImageRepository.findById(savedProfileImage.id!!).get()
            profileImageRepository.save(
                profileImage.update(newProfileImageUrl)
            )
            then("프로필 이미지가 새 URL로 변경되어야 한다") {
                profileImageRepository.findById(savedProfileImage.id!!).get().url.shouldBe("update")
            }
        }
    }
})