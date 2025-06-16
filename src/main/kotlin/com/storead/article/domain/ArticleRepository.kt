package com.storead.article.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ArticleRepository : JpaRepository<Article, UUID>, ArticleJoinRepository {
    fun findByTitle(title: String): Article?
    fun findByIdAndPublishStatus(articleId: UUID, publishStatus: ArticlePublishStatus): Article?
}