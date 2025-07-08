package com.storead.article.web.request

import com.storead.article.application.request.ArticlesPageServiceRequest
import java.util.*

data class MyArticlesRequest(
    private val limit: Int = 10,
    private val cursor: String? = null,
) {
    fun toServiceRequest(authorProfileId: UUID): ArticlesPageServiceRequest {
        return ArticlesPageServiceRequest(
            authorId = authorProfileId,
        )
    }

}