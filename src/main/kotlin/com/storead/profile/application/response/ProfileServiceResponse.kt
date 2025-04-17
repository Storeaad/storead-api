package com.storead.profile.application.response

import com.storead.profile.domain.Profile
import com.storead.profile.domain.ProfileImage
import java.util.UUID

data class ProfileServiceResponse(
    private val profile: Profile,
) {
    val id: UUID = profile.id
    val userId: UUID = profile.user!!.id
    val name: String = profile.profileName
    val aboutMe: String = profile.aboutMe
    val image: ProfileImage = profile.image ?: ProfileImage(url = "default Image")
}
