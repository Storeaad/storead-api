package com.storead.auth.domain

import com.storead.common.domain.BaseEntity
import com.storead.profile.domain.Profile
import jakarta.persistence.*
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime


@Entity
@Table(name = "users")
class User(

    @Column(nullable = false)
    val platformId: String,

    @Column(nullable = false)
    val email: String? = "",

    val name: String,

    @Enumerated(EnumType.STRING)
    val platform: PlatformType,

    var isActive: Boolean = true,

    @DateTimeFormat
    var lastLogin: LocalDateTime? = null,


    ) : BaseEntity() {

    fun updateLastLogin(lastLogin: LocalDateTime?) {
        this.lastLogin = lastLogin
    }

    fun toProfile(): Profile {
        return Profile(
            profileName = this.name,
            user = this
        )
    }
}