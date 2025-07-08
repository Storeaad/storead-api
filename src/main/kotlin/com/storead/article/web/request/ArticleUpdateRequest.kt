package com.storead.article.web.request

import com.storead.article.application.request.ArticleUpdateServiceRequest
import com.storead.article.domain.ArticlePublishStatus
import com.storead.article.domain.TagNames
import org.springframework.web.multipart.MultipartFile
import java.util.*

data class ArticleUpdateRequest(
    var title: String? = null,
    var description: String? = null,
    var body: String? = null,
    var publishStatus: ArticlePublishStatus? = null,
    val bookId: UUID? = null,
    var thumbnailImageFile: MultipartFile? = null,
    val tagNames: List<String>? = null

) {
    fun toServiceRequest(authorProfileId: UUID, articleId: UUID): ArticleUpdateServiceRequest {
        return ArticleUpdateServiceRequest(
            articleId = articleId,
            authorId = authorProfileId,
            title = title,
            description = description,
            body = body,
            tagNames = TagNames.from(tagNames),
            publishStatus = publishStatus,
            bookId = bookId,
            thumbnailImageFile = thumbnailImageFile
        )
    }


}