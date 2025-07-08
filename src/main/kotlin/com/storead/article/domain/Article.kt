package com.storead.article.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "articles")
class Article(

    @Column(name = "author_id", nullable = false)
    val authorProfileId: UUID,

    @Column(nullable = false, length = 50)
    var title: String,

    @Column(nullable = false, length = 100)
    var description: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var body: String,

    @Enumerated(EnumType.STRING)
    var publishStatus: ArticlePublishStatus = ArticlePublishStatus.CREATED,

    @Column(name = "book_id")
    var bookId: UUID? = null,

    @Column(name = "thumbnail_image_id")
    var thumbnailImageId: UUID? = null,

    ) : BaseEntity() {

    fun update(
        title: String? = null,
        description: String? = null,
        body: String? = null,
        publishStatus: ArticlePublishStatus? = null,
        bookId: UUID? = null,
        thumbnailImageId: UUID? = null
    ) {
        title?.let { this.title = it }
        description?.let { this.description = it }
        body?.let { this.body = it }
        publishStatus?.let { this.publishStatus = it }
        bookId?.let { this.bookId = it }
        thumbnailImageId?.let { this.thumbnailImageId = it }
    }

    fun doesNotOwner(userId: UUID): Boolean {
        return this.authorProfileId != userId
    }

    fun publish(): Article {
        this.publishStatus = ArticlePublishStatus.PUBLISHED
        return this
    }

    fun draft(): Article {
        this.publishStatus = ArticlePublishStatus.DRAFT
        return this
    }

    fun delete(): Article {
        this.publishStatus = ArticlePublishStatus.DELETED
        return this
    }
}