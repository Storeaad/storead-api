package com.storead.profile.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProfileImageRepository: JpaRepository<ProfileImage, UUID> {
}