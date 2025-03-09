package com.storead.profile.application

import com.storead.auth.signal.UserCreateEvent
import com.storead.common.storage.ImageFileHandler
import com.storead.profile.application.request.ProfileServiceUpdateRequest
import com.storead.profile.application.response.ProfileServiceResponse
import com.storead.profile.domain.Profile
import com.storead.profile.domain.ProfileImageRepository
import com.storead.profile.domain.ProfileRepository
import com.storead.profile.exception.ProfileException
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
    private val profileImageRepository: ProfileImageRepository,
    private val imageFileHandler: ImageFileHandler
) {
    @EventListener
    fun userCreateEventListen(event: UserCreateEvent) {
        create(
            event.instance.toProfile()
                .uploadProfileImage(event.profileImageUrl)
        )
    }

    fun getProfileByProfileId(profileId: Long): ProfileServiceResponse {
        val profile = profileRepository.findById(profileId).getOrNull() ?: throw ProfileException("프로필이 존재하지 않는 유저입니다.")
        return ProfileServiceResponse(profile)
    }


    fun getProfileByUserId(userId: Long): ProfileServiceResponse {
        val profile = profileRepository.findByUserId(userId) ?: throw ProfileException("프로필이 존재하지 않는 유저입니다.")
        return ProfileServiceResponse(profile)
    }

    fun updateProfile(request: ProfileServiceUpdateRequest): ProfileServiceResponse {
        val profile = profileRepository.findByUserId(request.userId)
            ?: throw IllegalArgumentException("프로필이 존재하지 않는 유저입니다.")

        val profileImage = request.imageFile?.let {
            val currentImageId = profile.image?.id ?: throw ProfileException("프로필 이미지 ID가 존재하지 않습니다.")
            val currentImage = profileImageRepository.findById(currentImageId)
                .getOrNull() ?: throw ProfileException("프로필 이미지를 찾을 수 없습니다.")

            profileImageRepository.save(
                currentImage.update(imageFileHandler.validate(it).saveImage(it).uri)
            )
        }

        return ProfileServiceResponse(
            profileRepository.save(profile.update(request, profileImage = profileImage))
        )
    }

    private fun create(profile: Profile): Profile {
        return profileRepository.save(profile)
    }
}