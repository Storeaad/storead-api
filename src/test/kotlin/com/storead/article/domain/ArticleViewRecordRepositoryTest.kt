package com.storead.article.domain

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*


@SpringBootTest
@ActiveProfiles("test")
@DisplayName("게시글 조회 기록 DB 영속성 테스트")
class ArticleViewRecordRepositoryTest(
    @Autowired private val repository: ArticleViewRecordRepository,
) : BehaviorSpec({

    afterSpec {
        repository.deleteAll()
    }

    given("인증된 사용자의 게시글 조회수가 기록 되어있으면") {
        val userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
        val articleId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001")

        val record = ArticleViewRecord.record(articleId = articleId, userId = userId)
        repository.save(record)

        `when`("게시글 아이디와 유저 아이디로 조회하면") {
            val result: ArticleViewRecord = repository.findById(record.id).get()
            then("유저가 조회한 기록이 존재해야한다") {
                result.userId shouldBe userId
                result.viewIp shouldBe null
            }
        }
    }

    given("인증 되지 않은 사용자의 게시글 조회수가 기록 되어있으면") {
        val ip = "0.0.0.1"
        val articleId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001")

        val record = ArticleViewRecord.record(articleId = articleId, viewIp = ip)
        repository.save(record)

        `when`("세기르 아이디와 접속한 유저의 아이피로 조회하면") {
            val result = repository.findById(record.id).get()
            then("유저가 조회한 기록이 존재해야한다") {
                result.viewIp shouldBe ip
                result.userId shouldBe null
            }
        }
    }

})