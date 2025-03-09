package com.storead.profile.application.response

import com.storead.profile.domain.Profile
import com.storead.profile.domain.ProfileImage

data class ProfileServiceResponse(
    private val profile: Profile,
) {
    val id: Long = profile.id!!
    val userId: Long = profile.user!!.id!!
    val name: String = profile.profileName
    val aboutMe: String = profile.aboutMe
    val image: ProfileImage = profile.image ?: ProfileImage(url = "default Image")
}
