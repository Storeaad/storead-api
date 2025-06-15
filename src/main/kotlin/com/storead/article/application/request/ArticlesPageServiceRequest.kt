package com.storead.article.application.request

import java.util.UUID

data class ArticlesPageServiceRequest(
    val limit: Int = 10,
    val cursor: UUID? = null,
    val authorId: UUID? = null,
) {

}
