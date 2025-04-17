package com.storead.profile.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID


interface ProfileRepository: JpaRepository<Profile, UUID> {
    fun findByUserId(userId: UUID): Profile?
}