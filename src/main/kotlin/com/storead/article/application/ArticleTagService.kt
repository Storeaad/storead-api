package com.storead.article.application

import com.storead.article.domain.ArticleTagRepository
import com.storead.article.signal.ArticleDeleteEvent
import com.storead.tag.application.TagService
import com.storead.tag.domain.TagNames
import com.storead.tag.domain.Tags
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener
import java.util.UUID


@Service
class ArticleTagService(
    private val tagService: TagService,
    private val articleTagRepository: ArticleTagRepository
) {

    @Async
    @TransactionalEventListener
    fun articleDeleted(event: ArticleDeleteEvent) {
        articleTagRepository.deleteAllByArticleId(event.articleId)
    }


    fun createArticleTags(articleId: UUID, tagNames: TagNames) {
        val tags: Tags = tagService.saveTags(tagNames)

        articleTagRepository.saveAll(tags.toArticleTags(articleId))
    }

    @Transactional
    fun updateArticleTags(articleId: UUID, tagNames: TagNames) {
        articleTagRepository.deleteAllByArticleId(articleId)

        createArticleTags(articleId, tagNames)
    }
}