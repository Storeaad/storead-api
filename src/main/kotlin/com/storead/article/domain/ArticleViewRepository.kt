package com.storead.article.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ArticleViewRepository : JpaRepository<ArticleView, UUID> {
    fun findByArticleId(articleId: UUID): ArticleView?
    fun deleteAllByArticleId(articleId: UUID)
}