package com.storead.article.domain

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.test.context.ActiveProfiles
import java.util.*

@ActiveProfiles("test")
@DisplayName("게시글 추천 도메인 테스트")
class RecommendTest() : BehaviorSpec({

    given("사용자가 게시글에 추천을 하려고 할 때") {
        val articleId = UUID.fromString("12345678-9012-3456-7890-123456789013")

        val recommend = Recommend(articleId)

        `when`("사용자가 추천 버튼을 클릭하면") {
            recommend.add()

            then("추천 수가 1 증가한다") {
                recommend.count shouldBe 1
            }
        }

        `when`("사용자가 추천을 취소하면") {
            recommend.remove()
            then("추천 수가 1 감소한다") {
                recommend.count shouldBe 0
            }
        }
    }
})