package com.storead.article.application.request

import com.storead.article.domain.ArticlePublishStatus
import com.storead.article.domain.TagNames
import org.springframework.web.multipart.MultipartFile
import java.util.*

data class ArticleUpdateServiceRequest(
    val articleId: UUID,
    val authorId: UUID,
    val title: String? = null,
    val body: String? = null,
    val description: String? = null,
    val publishStatus: ArticlePublishStatus? = null,
    val tagNames: TagNames? = null,
    val bookId: UUID? = null,
    val thumbnailImageFile: MultipartFile? = null,
)