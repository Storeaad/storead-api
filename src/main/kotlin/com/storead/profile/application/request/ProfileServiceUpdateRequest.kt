package com.storead.profile.application.request

import org.springframework.web.multipart.MultipartFile

data class ProfileServiceUpdateRequest(
    val userId: Long,
    val name: String? = null,
    val aboutMe: String? = null,
    val imageFile: MultipartFile? = null,
)