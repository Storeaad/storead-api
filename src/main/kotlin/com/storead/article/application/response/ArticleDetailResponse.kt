package com.storead.article.application.response

import com.storead.article.domain.ArticleDetailJoinResult
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
    val tagsName: List<String>
) {
    companion object {
        fun from(join: ArticleDetailJoinResult) = ArticleDetailResponse(
            articleId       = join.article.id,
            authorProfileId = join.authorProfileId,
            authorName      = join.authorProfileName,
            title           = join.article.title,
            description     = join.article.description,
            body            = join.article.body,
            publishStatus   = join.article.publishStatus.name,
            createdAt       = join.article.createdAt!!,
            tagsName        = join.tags
        )
    }
}