package com.storead.article.signal

import com.storead.article.domain.Article
import java.util.*

data class ArticleDeleteEvent(
    val articleId: UUID,
    val articleThumbnailImageId: UUID? = null,
) {
    companion object {
        fun from(article: Article) = ArticleDeleteEvent(article.id, article.thumbnailImageId)
    }
}