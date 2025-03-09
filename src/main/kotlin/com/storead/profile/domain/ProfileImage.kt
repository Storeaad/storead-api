package com.storead.profile.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.*


@Entity
@Table(name = "profile_images")
class ProfileImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "url")
    var url: String,
) : BaseEntity() {
    fun update(updateUrl: String): ProfileImage {
        this.url = updateUrl
        return this
    }
}