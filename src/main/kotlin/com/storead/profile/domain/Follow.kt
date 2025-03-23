package com.storead.profile.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "follows")
class Follow(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id")
    val from: Profile,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id")
    val to: Profile,


    ) : BaseEntity() {
}