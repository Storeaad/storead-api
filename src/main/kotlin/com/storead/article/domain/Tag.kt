package com.storead.article.domain

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.*


@Entity
@Table(name = "tags")
class Tag(

    @Column(nullable = false, length = 50, unique = true)
    val name: String,

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    val id: UUID? = null,
)


@Entity
@Table(name = "article_tags")
class Tagging(

    @Column(name = "article_id", nullable = false)
    val articleId: UUID,

    @Column(name = "tag_id", nullable = false)
    val tagId: UUID,

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    val id: UUID? = null,
)