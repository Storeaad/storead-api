package com.storead.profile.application.request

import org.springframework.web.multipart.MultipartFile
import java.util.UUID

data class ProfileServiceUpdateRequest(
    val userId: UUID,
    val name: String? = null,
    val aboutMe: String? = null,
    val imageFile: MultipartFile? = null,
)