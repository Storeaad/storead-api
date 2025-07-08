package com.storead.article.web.request

import com.storead.article.application.request.ArticleDeleteServiceRequest
import java.util.UUID

data class ArticleDeleteRequest(
    val articleId: UUID,
    val authorProfileId: UUID,
) {
    fun toServiceRequest() = ArticleDeleteServiceRequest(
        articleId = articleId,
        authorId = authorProfileId
    )
}