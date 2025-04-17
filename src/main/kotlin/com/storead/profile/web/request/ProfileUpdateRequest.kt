package com.storead.profile.web.request

import com.storead.profile.application.request.ProfileServiceUpdateRequest
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

data class ProfileUpdateRequest(
    val name: String? = null,
    val aboutMe: String? = null,
    val image: MultipartFile? = null,
) {
    fun toServiceRequest(userId: UUID) = ProfileServiceUpdateRequest(
        userId = userId,
        name = name,
        aboutMe = aboutMe,
        imageFile = image
    )
}
