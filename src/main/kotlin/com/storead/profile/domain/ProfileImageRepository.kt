package com.storead.profile.domain

import org.springframework.data.jpa.repository.JpaRepository

interface ProfileImageRepository: JpaRepository<ProfileImage, Long> {
}