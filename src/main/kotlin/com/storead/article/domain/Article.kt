package com.storead.article.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "articles")
class Article(

    @Column(name = "author_id", nullable = false)
    val authorID: UUID,

    @Column(nullable = false, length = 50)
    var title: String,

    @Column(nullable = false, length = 100)
    var description: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var body: String,

    @Enumerated(EnumType.STRING)
    var publishStatus: ArticlePublishStatus = ArticlePublishStatus.CREATED,

    @Column(name = "book_id")
    val bookID: UUID? = null,

    @Column(name = "thumbnail_image_id")
    var thumbnailImageID: UUID? = null,

    ) : BaseEntity() {

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
