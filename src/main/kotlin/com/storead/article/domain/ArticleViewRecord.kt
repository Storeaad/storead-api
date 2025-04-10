package com.storead.article.domain

import jakarta.persistence.Column
import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import java.time.LocalDate
import java.util.UUID

@RedisHash(value = "article_view_record", timeToLive = 24 * 60 * 60)
class ArticleViewRecord(

    @Id
    val id: String,

    val articleId: UUID,
    val userId: UUID? = null,
    val viewIp: String? = null,
    val viewDate: LocalDate = LocalDate.now(),
) {
    companion object {
        fun record(articleId: UUID, viewIp: String? = null, userId: UUID? = null): ArticleViewRecord {
            val id = userId?.let { "$it:$articleId" } ?: "$viewIp:$articleId"

            return ArticleViewRecord(
                id = id,
                articleId = articleId,
                userId = userId,
                viewIp = viewIp,
            )
        }
    }
}