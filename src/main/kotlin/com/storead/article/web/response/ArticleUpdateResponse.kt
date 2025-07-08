package com.storead.article.web.response

import com.storead.article.application.response.ArticleUpdateServiceResponse
import java.time.LocalDateTime
import java.util.UUID

data class ArticleUpdateResponse(
    val articleId: UUID,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(articleUpdateServiceResponse: ArticleUpdateServiceResponse) = ArticleUpdateResponse(
            articleId = articleUpdateServiceResponse.articleId,
            updatedAt = articleUpdateServiceResponse.updatedAt
        )
    }
}