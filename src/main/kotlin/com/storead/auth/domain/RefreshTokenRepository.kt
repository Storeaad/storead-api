package com.storead.auth.domain

import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface RefreshTokenRepository : CrudRepository<RefreshToken, UUID> {
    fun findByUserId(userId: UUID): RefreshToken?
}