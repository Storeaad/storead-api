package com.storead.auth.domain

import org.springframework.data.repository.CrudRepository

interface RefreshTokenRepository : CrudRepository<RefreshToken, String> {
    fun findByUserId(userId: Long): RefreshToken?
}