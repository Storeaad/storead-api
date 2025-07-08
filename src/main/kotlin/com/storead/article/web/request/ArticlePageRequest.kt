package com.storead.article.web.request

import com.storead.article.application.request.ArticlesPageServiceRequest
import java.util.UUID

data class ArticlePageRequest(
    private val limit: Int = 10,
    private val cursor: UUID? = null,

) {
    fun toServiceRequest(): ArticlesPageServiceRequest {
        return ArticlesPageServiceRequest(limit = limit, cursor = cursor)
    }
}