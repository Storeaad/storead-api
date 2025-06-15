package com.storead.article.signal

import java.util.UUID

data class ArticleRetrieveEvent(
    val articleId: UUID,
    val authorId: UUID? = null,
    val viewIp: String? = null
) {

}