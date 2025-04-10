package com.storead.article.domain

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.test.context.ActiveProfiles
import java.util.*


@ActiveProfiles("test")
@DisplayName("게시글 도메인 테스트")
class ArticleTest : BehaviorSpec({

    given("등록된 게시글이 존재할 때") {
        val authorId = UUID.randomUUID()

        val article = Article(
            authorId,
            "title",
            "description",
            "body",
        )

        `when`("게시글을 임시 저장 상태로 변경하면") {
            article.draft()

            then("게시글의 상태는 '임시 저장' 으로 변경되어야 한다") {
                article.publishStatus shouldBe ArticlePublishStatus.DRAFT
            }
        }

        `when`("게시글을 외부 사용자들이 읽을 수 있는 게시 상태로 변경하면") {
            article.publish()

            then("게시글의 상태는 '게시됨' 으로 변경되어야 한다") {
                article.publishStatus shouldBe ArticlePublishStatus.PUBLISHED
            }
        }

        `when`("사용자가 게시글을 삭제하면") {
            article.delete()

            then("게시글의 상태는 '삭제됨' 으로 변경되어야 한다") {
                article.publishStatus shouldBe ArticlePublishStatus.DELETED
            }
        }
    }
})