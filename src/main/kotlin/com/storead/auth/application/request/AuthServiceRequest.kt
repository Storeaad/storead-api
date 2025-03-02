package com.storead.auth.application.request

import com.storead.auth.domain.PlatformType
import com.storead.auth.domain.User

data class AuthServiceRequest(
    val name: String,
    val email: String,
    val platformId: String,
    val platform: PlatformType,
) {
    fun toEntity(): User {
        return User(
            name = name,
            email = email,
            platformId = platformId,
            platform = platform
        )
    }
}
