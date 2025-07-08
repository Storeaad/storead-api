package com.storead.article.web.request

import com.storead.article.application.request.ArticleCreateServiceRequest
import com.storead.article.domain.ArticlePublishStatus
import com.storead.article.domain.TagNames
import java.util.*

data class ArticleCreateRequest(
    var title: String,
    var description: String,
    var body: String,
    var publishStatus: ArticlePublishStatus = ArticlePublishStatus.CREATED,
    val bookId: UUID? = null,
    var thumbnailImageId: UUID? = null,
    val tagNames: List<String>? = null

) {
    fun toServiceRequest(authorProfileId: UUID): ArticleCreateServiceRequest {
        return ArticleCreateServiceRequest(
            userId = authorProfileId,
            title = title,
            description = description,
            body = body,
            tags = TagNames.from(tagNames),
            publishStatus = publishStatus,
            bookId = bookId,
            thumbnailImageFile = null
        )
    }
}