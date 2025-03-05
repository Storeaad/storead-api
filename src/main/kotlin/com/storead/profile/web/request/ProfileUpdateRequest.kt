package com.storead.profile.web.request

import com.storead.profile.application.request.ProfileServiceUpdateRequest

data class ProfileUpdateRequest(
    val name: String? = null,
    val aboutMe: String? = null,
    val image: String? = null,
) {
    fun toServiceRequest(userId: Long) = ProfileServiceUpdateRequest(
        userId = userId,
        name = name,
        aboutMe = aboutMe,
        image = image
    )
}
