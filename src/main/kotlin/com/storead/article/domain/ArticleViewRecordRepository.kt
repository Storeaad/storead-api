package com.storead.article.domain

import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ArticleViewRecordRepository : CrudRepository<ArticleViewRecord, String> {

    fun existsByArticleIdAndUserId(articleId: UUID, userId: UUID): Boolean

    fun existsByArticleIdAndViewIp(articleId: UUID, viewIp: String): Boolean

}