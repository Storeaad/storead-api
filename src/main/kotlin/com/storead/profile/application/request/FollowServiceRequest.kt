package com.storead.profile.application.request

data class FollowServiceRequest(
    val from: Long,
    val to: Long,
) {
    fun isSelfFollow() = from == to
}