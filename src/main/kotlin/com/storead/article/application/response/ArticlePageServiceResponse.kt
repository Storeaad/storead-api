package com.storead.article.application.response

import com.storead.article.application.request.ArticlesPageServiceRequest
import java.util.*

data class ArticlePageServiceResponse(
    private val allArticles: List<ArticleDetailServiceResponse>,
    private val articleRequest: ArticlesPageServiceRequest
) {
    private val requestSize = articleRequest.limit

    val articles = allArticles.take(requestSize)
    val nextCursor: UUID? = getCursor()

    private fun getCursor(): UUID? = articles.takeIf { hasNext() }?.lastOrNull()?.articleId

    private fun hasNext() = allArticles.size > requestSize
}