package com.storead.article.web.response

import com.storead.article.application.response.ArticleDetailServiceResponse
import com.storead.article.domain.ArticleBookDetail
import java.time.LocalDateTime
import java.util.*

data class ArticleDetailResponse(
    val articleId: UUID,
    val authorProfileId: UUID,
    val authorName: String,
    val title: String,
    val description: String,
    val body: String,
    val publishStatus: String,
    val createdAt: LocalDateTime,
    val tagsName: List<String>,
    val book: ArticleBookDetail?
) {
    companion object {
        fun from(serviceResponse: ArticleDetailServiceResponse) = ArticleDetailResponse(
            articleId = serviceResponse.articleId,
            authorProfileId = serviceResponse.authorProfileId,
            authorName = serviceResponse.authorName,
            title = serviceResponse.title,
            description = serviceResponse.description,
            body = serviceResponse.body,
            publishStatus = serviceResponse.publishStatus,
            createdAt = serviceResponse.createdAt,
            tagsName = serviceResponse.tagsName,
            book = serviceResponse.book
        )
    }
}