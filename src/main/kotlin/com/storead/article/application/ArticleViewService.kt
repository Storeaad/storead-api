package com.storead.article.application

import com.storead.article.domain.ArticleView
import com.storead.article.domain.ArticleViewRecord
import com.storead.article.domain.ArticleViewRecordRepository
import com.storead.article.domain.ArticleViewRepository
import com.storead.article.exception.ArticleError
import com.storead.article.exception.ArticleException
import com.storead.article.signal.ArticleCreateEvent
import com.storead.article.signal.ArticleDeleteEvent
import com.storead.article.signal.ArticleRetrieveEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener


@Service
class ArticleViewService(
    private val articleViewRecordRepository: ArticleViewRecordRepository,
    private val articleViewRepository: ArticleViewRepository,
) {

    @Async
    @TransactionalEventListener
    fun articleDelete(event: ArticleDeleteEvent) {
        articleViewRepository.deleteAllByArticleId(event.articleId)
    }


    @Async
    @TransactionalEventListener
    fun articleViewCreate(event: ArticleCreateEvent) {
        articleViewRepository.save(ArticleView(event.articleId))
    }

    @Async
    @EventListener
    fun articleView(event: ArticleRetrieveEvent) {

        if (isAlreadyWatchArticle(event)) {
            return
        }

        saveArticleViewRecord(event)
        incrementArticleViewCount(event)
    }

    private fun isAlreadyWatchArticle(event: ArticleRetrieveEvent): Boolean {
        return event.authorId?.let {
            articleViewRecordRepository.existsByArticleIdAndUserId(event.articleId, it)
        } ?: event.viewIp?.let {
            articleViewRecordRepository.existsByArticleIdAndViewIp(event.articleId, it)
        } ?: false
    }

    private fun saveArticleViewRecord(event: ArticleRetrieveEvent) {
        val articleViewRecord = ArticleViewRecord.record(event.articleId, event.viewIp, event.authorId)
        articleViewRecordRepository.save(articleViewRecord)
    }

    private fun incrementArticleViewCount(event: ArticleRetrieveEvent) {
        val articleView = articleViewRepository.findByArticleId(event.articleId)
            ?: throw ArticleException(ArticleError.ARTICLE_VIEW_NOT_FIND)

        articleView.update()
        articleViewRepository.save(articleView)
    }
}