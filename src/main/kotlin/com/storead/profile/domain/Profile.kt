package com.storead.profile.domain

import com.storead.common.domain.BaseEntity
import com.storead.profile.application.request.ProfileServiceUpdateRequest
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "profiles")
class Profile(

    @Column(name = "about_me")
    var aboutMe: String = "about me",

    @Column(name = "profile_name")
    var profileName: String,

    @Column(name = "user_id")
    val userId: UUID,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    var image: ProfileImage? = null,

    ) : BaseEntity() {

    fun uploadProfileImage(imageUrl: String): Profile {
        this.image = ProfileImage(url = imageUrl)
        return this

    }

    fun update(request: ProfileServiceUpdateRequest, profileImage: ProfileImage?): Profile {
        this.aboutMe = request.aboutMe ?: this.aboutMe
        this.profileName = request.name ?: this.profileName
        this.image = profileImage ?: this.image

        return this
    }
}