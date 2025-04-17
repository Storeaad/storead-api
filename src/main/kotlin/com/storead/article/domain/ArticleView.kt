package com.storead.article.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*


@Entity
@Table(name = "article_views")
class ArticleView(

    @Column(name = "article_id", unique = true, nullable = false)
    val articleId: UUID,

    var count: Int = 0,

    ) : BaseEntity() {

    fun update() {
        count += 1
    }

}