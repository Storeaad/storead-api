package com.storead.profile.application.response

import com.storead.profile.domain.Follow

data class FollowServiceResponse(
    val follow: Follow,
) {
    val fromUser: String = follow.from.profileName
    val toUser: String = follow.to.profileName
}
