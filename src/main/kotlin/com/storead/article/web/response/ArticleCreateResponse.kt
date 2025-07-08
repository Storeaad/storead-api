package com.storead.article.web.response

import com.storead.article.application.response.ArticleCreateServiceResponse
import java.time.LocalDateTime
import java.util.*

data class ArticleCreateResponse(
    val articleId: UUID,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(articleUpdateServiceResponse: ArticleCreateServiceResponse) = ArticleCreateResponse(
            articleId = articleUpdateServiceResponse.articleId,
            createdAt = articleUpdateServiceResponse.createdAt
        )
    }
}