package com.storead.article.domain

import com.github.f4b6a3.ulid.UlidCreator
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@ActiveProfiles("test")
@SpringBootTest
@DisplayName("게시글 태그 매핑 레포지토리 테스트")
class ArticleTagRepositoryTest(

    @Autowired val articleTagRepository: ArticleTagRepository,

    ) : BehaviorSpec({
    given("게시글에 태그가 등록 되어 있는 경우") {
        val articleID = UlidCreator.getMonotonicUlid().toUuid()
        val tagId = UlidCreator.getMonotonicUlid().toUuid()
        articleTagRepository.save(
            ArticleTag(articleID, tagId)
        )

        `when`("게시글 고유 아이디를 기준으로 조회하면") {
            val result = articleTagRepository.findByArticleId(articleID)
            then("게시글 아이디에 해당하는 태그 목록을 반환한다") {
                result.size shouldBe 1
                result.first().tagId shouldBe tagId
            }
        }
    }

    given("게시글에 태그가 없는 경우") {
        val articleID = UlidCreator.getMonotonicUlid().toUuid()

        `when`("게시글 고유 아이디로 조회하면") {
            val result = articleTagRepository.findByArticleId(articleID)

            then("비어있는 리스트가 반환된다")
                result.size shouldBe 0
                result.isEmpty() shouldBe true
        }

        `when`("비어있는 게시글 태그를 저장하면") {
            val result = articleTagRepository.saveAll(emptyList())

            then("아무것도 저장되지 않는다.") {
                result.isEmpty() shouldBe true
            }
        }
    }

})