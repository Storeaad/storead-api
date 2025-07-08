package com.storead.article.web.request

import com.storead.article.application.request.ArticleDetailServiceRequest
import java.util.UUID

data class ArticleDetailRequest(
    val id: UUID
) {
    fun toServiceRequest(): ArticleDetailServiceRequest {
        return ArticleDetailServiceRequest(
            articleId = id,
        )
    }
}