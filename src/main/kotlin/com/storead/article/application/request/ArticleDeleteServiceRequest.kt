package com.storead.article.application.request

import java.util.*

data class ArticleDeleteServiceRequest(
    val articleId: UUID,
    val authorId: UUID,
)