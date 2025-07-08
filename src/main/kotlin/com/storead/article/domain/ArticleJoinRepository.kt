package com.storead.article.domain

import java.util.*

data class ArticleDetailJoinResult(
    val article: Article,
    val authorProfileId: UUID,
    val authorProfileName: String,
    val tags: List<String>,
    val bookDetail: ArticleBookDetail?,
)

interface ArticleJoinRepository {

    fun findArticleDetailByArticleId(articleId: UUID): ArticleDetailJoinResult?
    fun findAllArticles(limit: Int, cursor: UUID? = null): List<ArticleDetailJoinResult>
    fun findAllArticlesByAuthorId(authorId: UUID, limit: Int, cursor: UUID? = null): List<ArticleDetailJoinResult>

}