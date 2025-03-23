package com.storead.profile.domain

import org.springframework.data.jpa.repository.JpaRepository

interface FollowRepository : JpaRepository<Follow, Long>, FollowPagingRepository {

    fun findByFromIdAndToId(from: Long, to: Long): Follow?

    fun existsByFromIdAndToId(from: Long, to: Long): Boolean

}