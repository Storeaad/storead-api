package com.storead.article.application

import com.storead.article.application.request.*
import com.storead.article.application.response.ArticleCreateServiceResponse
import com.storead.article.application.response.ArticleDetailServiceResponse
import com.storead.article.application.response.ArticlePageServiceResponse
import com.storead.article.application.response.ArticleUpdateServiceResponse
import com.storead.article.domain.Article
import com.storead.article.domain.ArticleDetailJoinResult
import com.storead.article.domain.ArticleRepository
import com.storead.article.domain.ArticleTagRepository
import com.storead.article.domain.ArticleThumbnailImageRepository
import com.storead.article.domain.ArticleViewRepository
import com.storead.article.exception.ArticleError
import com.storead.article.exception.ArticleException
import com.storead.article.signal.ArticleCreateEvent
import com.storead.article.signal.ArticleDeleteEvent
import com.storead.article.signal.ArticleRetrieveEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class ArticleService(
    private val articleTagService: ArticleTagService,
    private val articleRepository: ArticleRepository,
    private val thumbnailService: ArticleThumbnailService,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    fun createArticle(createRequest: ArticleCreateServiceRequest): ArticleCreateServiceResponse {
        val thumbnailImageId: UUID? = thumbnailService.upload(createRequest.thumbnailImageFile)?.id
        val createArticle = createRequest.toEntity(thumbnailImageId = thumbnailImageId)
        val article: Article = articleRepository.save(createArticle.publish())

        // NOTE: Article - Tag 매핑
        articleTagService.createArticleTags(article.id, createRequest.tagNames)

        // NOTE: 조회수 생성
        eventPublisher.publishEvent(ArticleCreateEvent(article.id))

        return ArticleCreateServiceResponse.from(article)
    }

    fun getAllArticles(request: ArticlesPageServiceRequest): ArticlePageServiceResponse = toPageResponse(
        articles = articleRepository.findAllArticles(request.limit + 1, request.cursor),
        request = request
    )

    fun getMyArticles(request: ArticlesPageServiceRequest): ArticlePageServiceResponse {
        val articleOwnerId: UUID = request.authorId ?: throw ArticleException(ArticleError.REQUIRE_AUTHOR_ID)

        return toPageResponse(
            articles = articleRepository.findAllArticlesByAuthorId(articleOwnerId, request.limit + 1, request.cursor),
            request = request
        )
    }

    fun getArticleDetail(request: ArticleDetailServiceRequest): ArticleDetailServiceResponse {
        val articleDetail: ArticleDetailJoinResult = articleRepository.findArticleDetailByArticleId(request.articleId)
            ?: throw ArticleException(ArticleError.ARTICLE_NOT_FOUND)

        eventPublisher.publishEvent(ArticleRetrieveEvent(articleDetail.article.id))

        return ArticleDetailServiceResponse.from(articleDetail)
    }

    @Transactional
    fun updateArticle(request: ArticleUpdateServiceRequest): ArticleUpdateServiceResponse {
        val article = getMyArticle(request.articleId, request.authorId)
        val thumbnailId: UUID? = thumbnailService.update(request.thumbnailImageFile)?.id

        request.tagNames?.let {
            articleTagService.updateArticleTags(article.id, request.tagNames)
        }

        article.update(
            request.title,
            request.description,
            request.body,
            request.publishStatus,
            request.bookId,
            thumbnailId
        )
        articleRepository.save(article)


        return ArticleUpdateServiceResponse.from(article)
    }

    @Transactional
    fun deleteArticle(request: ArticleDeleteServiceRequest) {
        val article = getMyArticle(request.articleId, request.authorId)

        article.delete()
        articleRepository.save(article)

        eventPublisher.publishEvent(ArticleDeleteEvent.from(article))
    }

    private fun toPageResponse(
        articles: List<ArticleDetailJoinResult>,
        request: ArticlesPageServiceRequest
    ): ArticlePageServiceResponse {
        return ArticlePageServiceResponse(
            articles.map {
                ArticleDetailServiceResponse.from(it)
            },
            request
        )
    }

    private fun getMyArticle(articleId: UUID, authorId: UUID): Article {
        val article: Article = articleRepository.findById(articleId)
            .orElseThrow { ArticleException(ArticleError.ARTICLE_NOT_FOUND) }

        if (article.doesNotOwner(authorId)) {
            throw ArticleException(ArticleError.HAS_NOT_OWNER)
        }

        return article
    }

}