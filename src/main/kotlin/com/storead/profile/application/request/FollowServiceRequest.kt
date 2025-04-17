package com.storead.profile.application.request

import java.util.UUID

data class FollowServiceRequest(
    val from: UUID,
    val to: UUID,
) {
    fun isSelfFollow() = from == to
}