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
    val bookId: UUID? = null,

    @Column(name = "thumbnail_image_id")
    var thumbnailImageId: UUID? = null,

    ) : BaseEntity() {

    fun update(title: String? = null, description: String? = null, body: String? = null) {
        title?.let { this.title = it }
        description?.let { this.description = it }
        body?.let { this.body = it }
    }

    fun doesNotOwner(userId: UUID): Boolean {
        return this.authorProfileId != userId
    }

    fun publish() {
        this.publishStatus = ArticlePublishStatus.PUBLISHED
    }

    fun draft() {
        this.publishStatus = ArticlePublishStatus.DRAFT
    }

    fun delete() {
        this.publishStatus = ArticlePublishStatus.DELETED
    }
}