package com.storead.tag.application

import com.github.f4b6a3.ulid.UlidCreator
import com.storead.IntegrationTestSupport
import com.storead.article.domain.Article
import com.storead.article.domain.ArticleRepository
import com.storead.article.domain.ArticleTagRepository
import com.storead.tag.domain.Tag
import com.storead.tag.domain.TagNames
import com.storead.tag.domain.TagRepository
import com.storead.tag.domain.Tags
import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

@DisplayName("태그 서비스 테스트")
class TagServiceTest(
    @Autowired private val tagService: TagService,
    @Autowired private val tagRepository: TagRepository,
    @Autowired private val articleRepository: ArticleRepository,
    @Autowired private val articleTagRepository: ArticleTagRepository,
) : IntegrationTestSupport({

    beforeContainer {
        tagRepository.deleteAll()
    }

    afterSpec {
        articleTagRepository.deleteAll()
        tagRepository.deleteAll()
    }

    given("사용자가 태그를 적용하려고 하는 경우") {
        val tagNames = TagNames(listOf("kotlin", "spring"))

        `when`("사용자가 입력한 태그를 생성하면") {
            tagService.saveTags(tagNames)

            then("태그가 모두 저장되어야한다") {
                val response = tagRepository.findAll()
                response.size shouldBe 2
                response.map { it.name } shouldContainExactlyInAnyOrder listOf("kotlin", "spring")
            }
        }

        `when`("이미 존재하는 태그와 새로운 태그를 같이 생성하면") {
            val tagNames = TagNames(listOf("kotlin", "spring", "python"))
            tagService.saveTags(tagNames)

            then("기존에 등록된 태그는 중복이 발생하면 안된다") {
                val response = tagRepository.findAll()
                response.size shouldBe 3
                response.map { it.name } shouldContainExactlyInAnyOrder listOf("kotlin", "spring", "python")
            }
        }
    }

    given("이미 작성된 게시글과 태그를 연결 하는 경우") {
        val article = articleRepository.saveAndFlush(
            Article(
                authorProfileId = UlidCreator.getMonotonicUlid().toUuid(),
                title = "test",
                description = "test",
                body = "test",
            )
        )

        `when`("주어진 태그 2개와 미리 작성 되어있던 게시글 1개를 연결 하면") {
            val tags = Tags(
                listOf(Tag("kotlin"), Tag("springboot"))
            )
            articleTagRepository.saveAll(tags.toArticleTags(article.id))

            then("게시글 태그 2개가 실제 저장되어야 한다") {
                articleTagRepository.findByArticleId(article.id).size shouldBe 2
            }
        }

    }

    given("사용자가 태그를 입력하지 않는 경우") {
        `when`("null 값을 저장하면") {
            tagService.saveTags(null)

            then("에러가 발생하지 않고, DB에 아무것도 저장되지 않아야한다.") {
                tagRepository.findAll().shouldBeEmpty()
            }
        }

        `when`("비어있는 태그 리스트를 저장하면") {
            tagService.saveTags(TagNames(emptyList()))

            then("에러가 발생하지 않고, DB에 아무것도 저장되지 않아야한다.") {
                tagRepository.findAll().shouldBeEmpty()
            }
        }

    }
})