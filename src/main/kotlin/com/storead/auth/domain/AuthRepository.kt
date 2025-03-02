package com.storead.auth.domain

import org.springframework.data.jpa.repository.JpaRepository

interface AuthRepository : JpaRepository<User, Long> {
    fun findByPlatformIdAndPlatform(platformId: String, platform: PlatformType): User?
}