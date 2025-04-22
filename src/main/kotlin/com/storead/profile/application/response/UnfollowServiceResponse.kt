package com.storead.profile.application.response

import com.storead.profile.domain.Profile

data class UnfollowServiceResponse(
    private val from: Profile,
    private val to: Profile,
) {
    val fromUser: String = from.profileName
    val toUser: String = to.profileName
}
