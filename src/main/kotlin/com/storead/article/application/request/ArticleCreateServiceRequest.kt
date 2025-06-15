package com.storead.article.application.request

import com.storead.article.domain.Article
import com.storead.article.domain.ArticlePublishStatus
import com.storead.article.domain.TagNames
import org.springframework.web.multipart.MultipartFile
import java.util.*

data class ArticleCreateServiceRequest(
    val userId: UUID,
    val title: String,
    val description: String,
    val body: String,
    val tags: TagNames,
    val publishStatus: ArticlePublishStatus = ArticlePublishStatus.PUBLISHED,
    val bookId: UUID? = null,
    val thumbnailImageFile: MultipartFile? = null,
) {
    fun toEntity(bookId: UUID? = null, thumbnailImageId: UUID? = null): Article {
        return Article(
            authorId = userId,
            title = title,
            description = description,
            body = body,
            publishStatus = publishStatus,
            bookId = bookId,
            thumbnailImageId = thumbnailImageId
        )
    }

}
