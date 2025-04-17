package com.storead.profile.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FollowRepository : JpaRepository<Follow, UUID>, FollowPagingRepository {

    fun findByFromIdAndToId(from: UUID, to: UUID): Follow?

    fun existsByFromIdAndToId(from: UUID, to: UUID): Boolean

}