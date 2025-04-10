package com.storead.article.domain

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.test.context.ActiveProfiles
import java.util.*

@ActiveProfiles("test")
@DisplayName("게시글 조회수 도메인 테스트")
class ArticleViewTest(): BehaviorSpec({

    given("사용자가 게시글을 읽은 경우") {
        val articleId = UUID.fromString("12345678-9012-3456-7890-123456789013")

        val articleView = ArticleView(articleId)

        `when`("게시글 조회수를 업데이트 하면") {
            articleView.update()
            then("게시글 조회수가 1 증가한다") {
                articleView.count shouldBe 1
            }
        }  
    }
})