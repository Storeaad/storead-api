package com.storead.article.domain

import com.storead.IntegrationTestSupport
import io.kotest.core.spec.DisplayName
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.util.*


@DisplayName("게시글 조회 기록 테스트")
class ArticleViewRecordTest() : IntegrationTestSupport({

    given("인증된 사용자가 게시글을 조회 하려고 할 때") {
        val givenArticleId = UUID.randomUUID()
        val givenUserId = UUID.randomUUID()

        `when`("게시글을 조회 하면") {
            val articleViewRecord = ArticleViewRecord.record(givenArticleId, userId = givenUserId)

            then("인증된 사용자의 유저 정보의 기록이 생성된다") {
                with(articleViewRecord) {
                    articleId shouldBe givenArticleId
                    userId shouldBe givenUserId
                    viewIp shouldBe null
                    viewDate shouldBe LocalDate.now()
                }
            }
        }
    }

    given("비인증 사용자가 게시글을 조회 하려고 할 때") {
        val givenArticleId = UUID.randomUUID()
        val viewIp = "0.0.0.1"

        `when`("게시글을 조회 하면") {
            val articleViewRecord = ArticleViewRecord.record(givenArticleId, viewIp = viewIp)
            then("비인증 사용자의 IP 정보의 기록이 생성된다") {
                with(articleViewRecord) {
                    articleId shouldBe givenArticleId
                    viewIp shouldBe "0.0.0.1"
                    viewDate shouldBe LocalDate.now()
                }
            }
        }
    }

})