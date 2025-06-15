package com.storead.article.application.request

import java.util.UUID

data class ArticleDeleteServiceRequest(
    val articleId: UUID,
    val authorId: UUID,
)
