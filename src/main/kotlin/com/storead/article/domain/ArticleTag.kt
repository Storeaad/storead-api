package com.storead.article.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "article_tags")
class ArticleTag(

    @Column(name = "article_id", nullable = false)
    val articleId: UUID,

    @Column(name = "tag_id", nullable = false)
    val tagId: UUID,

    ) : BaseEntity()