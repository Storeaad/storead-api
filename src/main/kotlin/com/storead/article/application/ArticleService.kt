package com.storead.article.application

import com.storead.article.application.request.*
import com.storead.article.application.response.ArticleDetailResponse
import com.storead.article.application.response.ArticlePageResponse
import com.storead.article.application.response.ArticleResponse
import com.storead.article.domain.Article
import com.storead.article.domain.ArticleDetailJoinResult
import com.storead.article.domain.ArticleRepository
import com.storead.article.domain.Tags
import com.storead.article.exception.ArticleError
import com.storead.article.exception.ArticleException
import com.storead.article.signal.ArticleCreateEvent
import com.storead.article.signal.ArticleRetrieveEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class ArticleService(
    private val tagService: TagService,
    private val articleRepository: ArticleRepository,
    private val thumbnailService: ArticleThumbnailService,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    fun createArticle(createRequest: ArticleCreateServiceRequest): ArticleResponse {
        val thumbnailImageId: UUID? = thumbnailService.upload(createRequest.thumbnailImageFile)?.id
        val article: Article = articleRepository.save(createRequest.toEntity(thumbnailImageId = thumbnailImageId))

        val tags: Tags = tagService.addAll(createRequest.tags)
        tagService.tagMappingWithArticle(tags, article.id)

        // NOTE: 조회수 생성
        eventPublisher.publishEvent(ArticleCreateEvent(article.id))

        return ArticleResponse.from(article)
    }

    @PreAuthorize("permitAll()")
    fun getAllArticles(request: ArticlesPageServiceRequest): ArticlePageResponse = toPageResponse(
        articles = articleRepository.findAllArticles(request.limit + 1, request.cursor),
        request = request
    )

    fun getMyArticles(request: ArticlesPageServiceRequest): ArticlePageResponse {
        val articleOwnerId: UUID = request.authorId ?: throw ArticleException(ArticleError.REQUIRE_AUTHOR_ID)

        return toPageResponse(
            articles = articleRepository.findAllArticlesByAuthorId(articleOwnerId, request.limit + 1, request.cursor),
            request = request
        )
    }

    @PreAuthorize("permitAll()")
    fun getArticleDetail(request: ArticleDetailServiceRequest): ArticleDetailResponse {
        val articleDetail: ArticleDetailJoinResult = articleRepository.findArticleDetailByArticleId(request.articleId)
            ?: throw ArticleException(ArticleError.ARTICLE_NOT_FOUND)

        eventPublisher.publishEvent(ArticleRetrieveEvent(articleDetail.article.id))

        return ArticleDetailResponse.from(articleDetail)
    }

    fun updateArticle(request: ArticleUpdateServiceRequest): ArticleResponse {
        val article = getMyArticle(request.articleId, request.authorId)

        article.update(request.title, request.description, request.body)
        articleRepository.save(article)

        return ArticleResponse.from(article)
    }

    fun deleteArticle(request: ArticleDeleteServiceRequest): ArticleResponse {
        val article = getMyArticle(request.articleId, request.authorId)

        article.delete()
        articleRepository.save(article)

        return ArticleResponse.from(article)
    }

    private fun toPageResponse(
        articles: List<ArticleDetailJoinResult>,
        request: ArticlesPageServiceRequest
    ): ArticlePageResponse {
        return ArticlePageResponse(
            articles.map {
                ArticleDetailResponse.from(it)
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