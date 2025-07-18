package com.storead.article.application

import com.github.f4b6a3.ulid.UlidCreator
import com.storead.IntegrationTestSupport
import com.storead.article.domain.ArticleView
import com.storead.article.domain.ArticleViewRecord
import com.storead.article.domain.ArticleViewRecordRepository
import com.storead.article.domain.ArticleViewRepository
import com.storead.article.signal.ArticleRetrieveEvent
import io.kotest.core.annotation.DisplayName
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode.Root
import io.kotest.matchers.shouldBe
import org.awaitility.Awaitility.await
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.TimeUnit

@DisplayName("게시글 조회수 테스트")
class ArticleViewTest(
    @Autowired private val articleViewService: ArticleViewService,
    @Autowired private val articleViewRepository: ArticleViewRepository,
    @Autowired private val articleViewRecordRepository: ArticleViewRecordRepository,

    ) : IntegrationTestSupport({

    extensions(SpringTestExtension(Root))

    afterSpec {
        articleViewRepository.deleteAll()
    }

    given("작성된 게시글이 있는 경우") {

        val authorId = UlidCreator.getMonotonicUlid().toUuid()
        val articleId = UlidCreator.getMonotonicUlid().toUuid()

        articleViewRepository.saveAndFlush(
            ArticleView(articleId)
        )

        val request = ArticleRetrieveEvent(articleId = articleId, authorId = authorId, viewIp = "127.0.0.1")

        `when`("게시글을 조회 하면") {
            articleViewService.articleView(request)

            then("조회수가 1 증가 해야한다") {
                await()
                    .atMost(1, TimeUnit.SECONDS)
                    .pollDelay(50, TimeUnit.MILLISECONDS)
                    .untilAsserted { articleViewRepository.findByArticleId(articleId)?.count shouldBe 1 }
            }
        }
    }

    given("동일한 사용자가 게시글을 조회 한 적이 있는 경우") {
        val authorId = UlidCreator.getMonotonicUlid().toUuid()
        val articleId = UlidCreator.getMonotonicUlid().toUuid()

        val request = ArticleRetrieveEvent(articleId = articleId, authorId = authorId, viewIp = "127.0.0.1")

        articleViewRepository.saveAndFlush(
            ArticleView(articleId)
        )

        articleViewRecordRepository.save(
            ArticleViewRecord.record(
                request.articleId, request.viewIp, request.authorId
            )
        )

        `when`("동일한 사용자가 24시간 이내 게시글을 다시 조회 하면") {
            articleViewService.articleView(request)

            then("조회수는 변하지 않는다") {
                await()
                    .atMost(1, TimeUnit.SECONDS)
                    .pollDelay(50, TimeUnit.MILLISECONDS)
                    .untilAsserted { articleViewRepository.findByArticleId(articleId)?.count shouldBe 1 }
            }
        }
    }
})