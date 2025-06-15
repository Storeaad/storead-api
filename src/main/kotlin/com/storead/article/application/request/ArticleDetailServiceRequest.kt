package com.storead.article.application.request

import com.storead.article.domain.ArticlePublishStatus
import java.util.UUID

data class ArticleDetailServiceRequest(
    val articleId: UUID,
    val publishStatus: ArticlePublishStatus = ArticlePublishStatus.PUBLISHED
) {

}
