package com.storead.profile.web.request

import com.storead.profile.application.request.ProfileServiceUpdateRequest
import org.springframework.web.multipart.MultipartFile

data class ProfileUpdateRequest(
    val name: String? = null,
    val aboutMe: String? = null,
    val image: MultipartFile? = null,
) {
    fun toServiceRequest(userId: Long) = ProfileServiceUpdateRequest(
        userId = userId,
        name = name,
        aboutMe = aboutMe,
        imageFile = image
    )
}
