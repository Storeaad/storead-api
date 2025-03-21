package com.storead.profile.web.request

data class FollowingRequest(
    val fromProfileId: Long,
    val cursor: Long? = null,
    val limit: Int = 10,
)
