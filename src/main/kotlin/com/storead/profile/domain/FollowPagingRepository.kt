package com.storead.profile.domain

import java.util.*

interface FollowPagingRepository {
    fun findFollowingByFromId(profileId: Long, limit: Int, cursor: Long? = null): List<Follow>
    fun findFollowersByToId(profileId: Long, limit: Int, cursor: Long? = null): List<Follow>

}