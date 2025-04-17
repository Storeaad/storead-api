package com.storead.auth.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AuthRepository : JpaRepository<User, UUID> {
    fun findByPlatformIdAndPlatform(platformId: String, platform: PlatformType): User?
}