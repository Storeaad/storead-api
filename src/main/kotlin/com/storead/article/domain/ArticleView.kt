package com.storead.article.domain

import com.storead.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "article_views")
class ArticleView(

    @Column(name = "article_id", unique = true, nullable = false)
    val articleId: UUID,

    var count: Int = 0,

    @Id
    val id: UUID? = null
) : BaseEntity() {

    fun update() {
        count += 1
    }

}