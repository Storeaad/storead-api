package com.storead.article.application.request

import java.util.UUID

data class ArticleUpdateServiceRequest(
    val articleId: UUID,
    val authorId: UUID,
    val title: String? = null,
    val body: String? = null,
    val description: String? = null,
    val publishStatus: String? = null,

//    val imageUrls: List<String>? = null,
    // TODO: Add tags
)