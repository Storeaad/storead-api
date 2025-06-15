package com.storead.article.application.response

import com.storead.article.domain.Article
import java.util.UUID

data class ArticleResponse(
    private val articleId: UUID,
    private val title: String,
) {
    companion object {
        fun from(article: Article) = ArticleResponse(
            article.id,
            article.title
        )
    }
}