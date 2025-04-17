package com.storead.profile.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table


@Entity
@Table(name = "profile_images")
class ProfileImage(

    @Column(name = "url")
    var url: String,

    ) : BaseEntity() {
    fun update(updateUrl: String): ProfileImage {
        this.url = updateUrl
        return this
    }
}