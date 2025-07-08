package com.storead.article.application.response

import com.storead.article.domain.Article
import java.time.LocalDateTime
import java.util.*

data class ArticleCreateServiceResponse(
    val articleId: UUID,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(article: Article) = ArticleCreateServiceResponse(
            article.id,
            article.createdAt!!
        )
    }
}