package com.storead.article.application.response

import com.storead.article.domain.Article
import com.storead.article.domain.ArticlePublishStatus
import java.util.*

data class ArticleResponse(
    val articleId: UUID,
    val title: String,
    val description: String,
    val body: String,
    val publishStatus: ArticlePublishStatus,
    ) {
    companion object {
        fun from(article: Article) = ArticleResponse(
            article.id,
            article.title,
            article.description,
            article.body,
            article.publishStatus,
        )
    }
}