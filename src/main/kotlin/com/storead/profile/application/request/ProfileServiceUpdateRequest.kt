package com.storead.profile.application.request

data class ProfileServiceUpdateRequest(
    val userId: Long,
    val name: String? = null,
    val aboutMe: String? = null,
    val image: String? = null,
)