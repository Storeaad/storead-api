package com.storead.article.web.response

import com.storead.article.application.response.ArticlePageServiceResponse
import java.util.*

data class ArticlePageResponse(
    val articles: List<ArticleDetailResponse>,
    val nextCursor: UUID?
) {
    companion object {
        fun from(serviceResponse: ArticlePageServiceResponse) = ArticlePageResponse(
            articles = serviceResponse.articles.map { ArticleDetailResponse.from(it) },
            nextCursor = serviceResponse.nextCursor
        )
    }
}