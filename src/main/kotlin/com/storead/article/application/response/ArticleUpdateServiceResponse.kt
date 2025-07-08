package com.storead.article.application.response

import com.storead.article.domain.Article
import java.time.LocalDateTime
import java.util.*

data class ArticleUpdateServiceResponse(
    val articleId: UUID,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(article: Article) = ArticleUpdateServiceResponse(
            article.id,
            article.lastModifiedAt!!
        )
    }
}