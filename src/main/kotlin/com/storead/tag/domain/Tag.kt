package com.storead.tag.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table


@Entity
@Table(name = "tags")
class Tag(

    @Column(nullable = false, length = 50, unique = true)
    val name: String,
) : BaseEntity()