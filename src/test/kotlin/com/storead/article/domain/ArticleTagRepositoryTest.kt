package com.storead.article.domain

import com.github.f4b6a3.ulid.UlidCreator
import com.storead.IntegrationTestSupport
import io.kotest.core.spec.DisplayName
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode.Root
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional


@DisplayName("게시글 태그 매핑 레포지토리 테스트")
@Transactional
class ArticleTagRepositoryTest(

    @Autowired val articleTagRepository: ArticleTagRepository,

    ) : IntegrationTestSupport({

    extensions(SpringTestExtension(Root))


    given("게시글에 태그가 등록 되어 있는 경우") {
        val articleId = UlidCreator.getMonotonicUlid().toUuid()
        val tagId = UlidCreator.getMonotonicUlid().toUuid()
        articleTagRepository.save(
            ArticleTag(articleId, tagId)
        )

        `when`("게시글 고유 아이디를 기준으로 조회하면") {
            val result = articleTagRepository.findByArticleId(articleId)
            then("게시글 아이디에 해당하는 태그 목록을 반환한다") {
                result.size shouldBe 1
                result.first().tagId shouldBe tagId
            }
        }

        `when`("특정 게시글을 삭제 하면") {
            articleTagRepository.deleteAllByArticleId(articleId)

            then("해당 게시글 매핑 된 태그는 모두 삭제된다.") {
                articleTagRepository.findByArticleId(articleId).shouldBeEmpty()
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