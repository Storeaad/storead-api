package com.storead.article.domain

import com.github.f4b6a3.ulid.UlidCreator
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@ActiveProfiles("test")
@SpringBootTest
@DisplayName("게시글 조회 레포지토리 테스트")
class ArticleViewRepositoryTest(

    @Autowired private val articleViewRepository: ArticleViewRepository,

    ) : BehaviorSpec({
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
})