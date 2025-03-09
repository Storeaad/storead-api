package com.storead.auth.web.response

import com.storead.auth.domain.PlatformType
import com.storead.auth.domain.User

data class LoginResponse(
    private val user: User
) {
    val name: String = user.name
    val platform: PlatformType = user.platform

}
