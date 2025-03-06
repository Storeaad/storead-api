package com.storead.profile.application

import com.storead.auth.signal.UserCreateEvent
import com.storead.profile.application.request.ProfileServiceUpdateRequest
import com.storead.profile.application.response.ProfileServiceResponse
import com.storead.profile.domain.Profile
import com.storead.profile.domain.ProfileRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
) {
    @EventListener
    fun userCreateEventListen(event: UserCreateEvent) {
        create(event.instance.toProfile())
        create(
            event.instance.toProfile()
                .uploadProfileImage(event.profileImageUrl)
        )
    }

    fun getMyProfile(userId: Long): ProfileServiceResponse {
        val profile = profileRepository.findByUserId(userId) ?: throw IllegalArgumentException("프로필이 존재하지 않는 유저입니다.")
        return ProfileServiceResponse(profile)
    }

    fun updateProfile(request: ProfileServiceUpdateRequest): ProfileServiceResponse {
        val profile = profileRepository.findByUserId(request.userId) ?: throw IllegalArgumentException("프로필이 존재하지 않는 유저입니다.")

        return ProfileServiceResponse(
            profileRepository.save(profile.update(request, profileImage = profileImage))
        )
    }

    private fun create(profile: Profile): Profile {
        return profileRepository.save(profile)

    }
}