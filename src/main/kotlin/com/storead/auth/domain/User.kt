package com.storead.auth.domain

import com.storead.profile.domain.Profile
import jakarta.persistence.*


@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val platformId: String,

    @Column(nullable = false)
    val email: String? = "",

    val name: String,

    @Enumerated(EnumType.STRING)
    val platform: PlatformType,
) {
    fun toProfile(): Profile {
        return Profile(
            profileName = this.name,
            user = this
        )
    }
}