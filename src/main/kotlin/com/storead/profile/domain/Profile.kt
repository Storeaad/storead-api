package com.storead.profile.domain

import com.storead.auth.domain.User
import com.storead.profile.application.request.ProfileServiceUpdateRequest
import jakarta.persistence.*

@Entity
@Table(name = "profiles")
class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "about_me")
    var aboutMe: String = "about me",

    @Column(name = "profile_name")
    var profileName: String,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    var image: ProfileImage? = null,

    ) {
    fun update(request: ProfileServiceUpdateRequest): Profile {
        this.aboutMe = request.aboutMe ?: this.aboutMe
        this.profileName = request.name ?: this.profileName
        this.image = request.image?.let { ProfileImage(url = it) } ?: this.image

        return this
    }
}