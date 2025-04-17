package com.storead.profile.application.request

import java.util.UUID

data class FollowRelationshipServiceRequest(
    val from: UUID,
    val limit: Int,
    val cursor: UUID?
)
