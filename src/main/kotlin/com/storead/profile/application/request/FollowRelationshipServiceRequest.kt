package com.storead.profile.application.request

data class FollowRelationshipServiceRequest(
    val from: Long,
    val limit: Int,
    val cursor: Long?
)
