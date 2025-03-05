package com.storead.profile.domain

import jakarta.persistence.*


@Entity
@Table(name = "profile_images")
class ProfileImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "url")
    val url: String,
    )