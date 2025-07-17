package com.storead.article.domain

import com.github.f4b6a3.ulid.UlidCreator
import com.storead.IntegrationTestSupport
import io.kotest.core.annotation.DisplayName
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode.Root
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.util.*


@DisplayName("게시글 조회 레포지토리 테스트")
@Transactional
class ArticleViewRepositoryTest(

    @Autowired private val articleViewRepository: ArticleViewRepository,

    ) : IntegrationTestSupport({

    extensions(SpringTestExtension(Root))

    given("사용자가 1번 읽은 게시글 조회수를 조회하는 경우") {
        val articleId = UlidCreator.getMonotonicUlid().toUuid()
        articleViewRepository.save(ArticleView(articleId, 1))


        `when`("게시글 아이디로 조회수를 요청하면") {
            val result = articleViewRepository.findByArticleId(articleId)

            then("게시글 고유 아이디와 증가된 조회수가 반환된다") {
                result?.count shouldBe 1
                result?.articleId shouldBe articleId
            }
        }
    }

    given("사용자가 읽지 않은 게시글의 조회수를 조회 하는 경우") {
        val articleId = UlidCreator.getMonotonicUlid().toUuid()
        articleViewRepository.save(ArticleView(articleId))

        `when`("게시글 아이디로 조회수를 요청하면") {
            val result = articleViewRepository.findByArticleId(articleId)

            then("게시글 고유 아이디와 조회수 0이 반환된다") {
                result?.count shouldBe 0
                result?.articleId shouldBe articleId
            }
        }
    }

    given("사용자의 게시글이 삭제된 경우") {
        val articleId = UUID.randomUUID()
        articleViewRepository.save(ArticleView(articleId))

        `when`("사용자가 게시글을 삭제하면") {
            articleViewRepository.deleteByArticleId(articleId)

            then("게시글 조회수가 삭제된다.") {
                articleViewRepository.findByArticleId(articleId) shouldBe null
            }

        }
    }

})