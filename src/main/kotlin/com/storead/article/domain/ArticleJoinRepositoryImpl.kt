package com.storead.article.domain

import com.querydsl.core.Tuple
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.storead.book.domain.QBook
import com.storead.profile.domain.QProfile
import com.storead.tag.domain.QTag
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ArticleJoinRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : ArticleJoinRepository {

    private val article = QArticle.article
    private val articleTag = QArticleTag.articleTag
    private val tag = QTag.tag
    private val profile = QProfile.profile
    private val book = QBook.book

    private val orderByCreatedDesc = article.createdAt.desc()

    override fun findAllArticles(
        limit: Int,
        cursor: UUID?
    ): List<ArticleDetailJoinResult> {

        val isPublished = article.publishStatus.eq(ArticlePublishStatus.PUBLISHED)

        val condition = cursor?.let {
            isPublished.and(article.id.lt(it))
        } ?: isPublished

        return toListQueryResult(
            joinQuery()
                .where(condition)
                .orderBy(orderByCreatedDesc)
                .limit(limit.toLong())
        )
    }

    override fun findAllArticlesByAuthorId(
        authorId: UUID,
        limit: Int,
        cursor: UUID?
    ): List<ArticleDetailJoinResult> {

        val isAuthorPublished = article.publishStatus
            .eq(ArticlePublishStatus.PUBLISHED)
            .and(article.authorProfileId.eq(authorId))

        val condition = cursor?.let {
            isAuthorPublished.and(article.id.lt(it))
        } ?: isAuthorPublished

        return toListQueryResult(
            joinQuery()
                .where(condition)
                .orderBy(orderByCreatedDesc)
                .limit(limit.toLong())
        )
    }

    override fun findArticleDetailByArticleId(articleId: UUID): ArticleDetailJoinResult? {
        return toSingleOptionalQueryResult(
            joinQuery()
                .where(article.id.eq(articleId)),
            articleId = articleId
        )

    }

    private fun joinQuery(): JPAQuery<Tuple> = queryFactory
        .select(
            article.id,
            article.title,
            article.description,
            article.body,
        )
        .from(article)
        .leftJoin(profile)
        .on(article.authorProfileId.eq(profile.id))
        .leftJoin(articleTag)
        .on(article.id.eq(articleTag.articleId))
        .leftJoin(tag)
        .on(articleTag.tagId.eq(tag.id))
        .leftJoin(book)
        .on(article.bookId.eq(book.id))


    private fun toListQueryResult(query: JPAQuery<Tuple>): List<ArticleDetailJoinResult> = query
        .transform(
            groupBy(article.id).list(
                Projections.constructor(
                    ArticleDetailJoinResult::class.java,
                    article,
                    profile.id,
                    profile.profileName,
                    list(tag.name),
                    Projections.constructor(
                        ArticleBookDetail::class.java,
                        book.id,
                        book.title,
                        book.author,
                        book.description,
                        book.image,
                        book.isbn,
                        book.publishDate,
                    )
                )
            )
        )

    private fun toSingleOptionalQueryResult(query: JPAQuery<Tuple>, articleId: UUID): ArticleDetailJoinResult? = query
        .transform(
            groupBy(article.id).`as`(
                Projections.constructor(
                    ArticleDetailJoinResult::class.java,
                    article,
                    profile.id,
                    profile.profileName,
                    list(tag.name),
                    Projections.constructor(
                        ArticleBookDetail::class.java,
                        book.id,
                        book.title,
                        book.author,
                        book.description,
                        book.image,
                        book.isbn,
                        book.publishDate,
                    )
                )
            )
        )[articleId]


}