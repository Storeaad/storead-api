package com.storead.profile.application

import com.storead.auth.signal.UserCreateEvent
import com.storead.common.storage.LocalImageFileHandler
import com.storead.common.storage.UploadFile
import com.storead.profile.application.request.ProfileServiceUpdateRequest
import com.storead.profile.application.response.ProfileServiceResponse
import com.storead.profile.domain.Profile
import com.storead.profile.domain.ProfileImageRepository
import com.storead.profile.domain.ProfileRepository
import com.storead.profile.exception.ProfileException
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
    private val profileImageRepository: ProfileImageRepository,
    private val imageFileHandler: LocalImageFileHandler
) {
    @EventListener
    fun userCreateEventListen(event: UserCreateEvent) {
        create(
            event.instance.toProfile()
                .uploadProfileImage(event.profileImageUrl)
        )
    }

    fun getProfileByProfileId(profileId: UUID): ProfileServiceResponse {
        val profile = profileRepository.findById(profileId).getOrNull() ?: throw ProfileException("프로필이 존재하지 않는 유저입니다.")
        return ProfileServiceResponse(profile)
    }


    fun getProfileByUserId(userId: UUID): ProfileServiceResponse {
        val profile = profileRepository.findByUserId(userId) ?: throw ProfileException("프로필이 존재하지 않는 유저입니다.")
        return ProfileServiceResponse(profile)
    }

    @Transactional
    fun updateProfile(request: ProfileServiceUpdateRequest): ProfileServiceResponse {
        val profile = profileRepository.findByUserId(request.userId)
            ?: throw IllegalArgumentException("프로필이 존재하지 않는 유저입니다.")

        val profileImage = request.imageFile?.let {
            val currentImageId = profile.image?.id ?: throw ProfileException("프로필 이미지 ID가 존재하지 않습니다.")
            val currentImage = profileImageRepository.findById(currentImageId)
                .getOrNull() ?: throw ProfileException("프로필 이미지를 찾을 수 없습니다.")

            val uploadFile = UploadFile(it)
            profileImageRepository.save(
                currentImage.update(
                    imageFileHandler
                        .validate(uploadFile)
                        .saveImage(uploadFile).uri)
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