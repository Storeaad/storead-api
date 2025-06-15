package com.storead.article.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ArticleTagRepository : JpaRepository<ArticleTag, UUID> {
    fun findByArticleId(articleId: UUID): List<ArticleTag>
}