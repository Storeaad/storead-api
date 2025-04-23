package com.storead.profile.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "follows")
class Follow(

    @Column(name = "follower_id", nullable = false)
    val fromId: UUID,

    @Column(name = "following_id", nullable = false)
    val toId: UUID,

    ) : BaseEntity() {
}