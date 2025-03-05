package com.storead.auth.signal

import com.storead.auth.domain.User

data class UserCreateEvent(
    val instance: User
)
