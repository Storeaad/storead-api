package com.storead.article.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*


@Entity
@Table(name = "tags")
class Tag(

    @Column(nullable = false, length = 50, unique = true)
    val name: String,
) : BaseEntity()


@Entity
@Table(name = "article_tags")
class Tagging(

    @Column(name = "article_id", nullable = false)
    val articleId: UUID,

    @Column(name = "tag_id", nullable = false)
    val tagId: UUID,

) : BaseEntity()