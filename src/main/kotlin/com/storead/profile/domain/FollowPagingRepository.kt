package com.storead.profile.domain

import java.util.*

interface FollowPagingRepository {
    fun findFollowingByFromId(profileId: UUID, limit: Int, cursor: UUID? = null): List<Follow>
    fun findFollowersByToId(profileId: UUID, limit: Int, cursor: UUID? = null): List<Follow>

}