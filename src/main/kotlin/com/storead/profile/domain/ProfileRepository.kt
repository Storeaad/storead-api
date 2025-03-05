package com.storead.profile.domain

import org.springframework.data.jpa.repository.JpaRepository


interface ProfileRepository: JpaRepository<Profile, Long> {
    fun findByUserId(userId: Long): Profile?
}