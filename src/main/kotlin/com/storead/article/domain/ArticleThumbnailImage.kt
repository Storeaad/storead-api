package com.storead.article.domain

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.UUID


@Entity
@Table(name = "article_thumbnails")
class ArticleThumbnailImage(

    val thumbnailUrl: String,

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    val id: UUID? = null,
) {


}