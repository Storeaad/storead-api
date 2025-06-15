package com.storead.article.application.response

import com.storead.article.application.request.ArticlesPageServiceRequest
import java.util.*

data class ArticlePageResponse(
    private val allArticles: List<ArticleDetailResponse>,
    private val articleRequest: ArticlesPageServiceRequest
) {
    private val requestSize = articleRequest.limit

    val articles = allArticles.take(requestSize)
    val nextCursor: UUID? = getCursor()

    private fun getCursor(): UUID? = articles.takeIf { hasNext() }?.lastOrNull()?.articleId

    private fun hasNext() = allArticles.size > requestSize
}
