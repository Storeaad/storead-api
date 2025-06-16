package com.storead.article.application

import com.github.f4b6a3.ulid.UlidCreator
import com.storead.article.domain.Article
import com.storead.article.domain.ArticleRepository
import com.storead.article.domain.ArticleTagRepository
import com.storead.article.domain.Tag
import com.storead.article.domain.TagNames
import com.storead.article.domain.TagRepository
import com.storead.article.domain.Tags
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@SpringBootTest
@ActiveProfiles("test")
@DisplayName("태그 서비스 테스트")
class TagServiceTest(
    @Autowired private val tagService: TagService,
    @Autowired private val tagRepository: TagRepository,
    @Autowired private val articleRepository: ArticleRepository,
    @Autowired private val articleTagRepository: ArticleTagRepository,
) : BehaviorSpec({

    lateinit var article: Article

    beforeSpec {
        article = articleRepository.saveAndFlush(
            Article(
                authorProfileId = UlidCreator.getMonotonicUlid().toUuid(),
                title = "test",
                description = "test",
                body = "test",
            )
        )
    }

    afterSpec {
        articleRepository.deleteAll()
        tagRepository.deleteAll()
    }

    given("사용자가 태그를 적용하려고 하는 경우") {
        val tagNames = TagNames(listOf("kotlin", "spring"))

        `when`("사용자가 입력한 태그를 생성하면") {
            tagService.addAll(tagNames)

            then("태그가 모두 저장되어야한다") {
                val response = tagRepository.findAll()
                response.size shouldBe 2
                response.map { it.name } shouldBe listOf("kotlin", "spring")
            }
        }

        `when`("이미 존재하는 태그와 새로운 태그를 같이 생성하면") {
            val tagNames = TagNames(listOf("kotlin", "spring", "python"))
            tagService.addAll(tagNames)

            then("기존에 등록된 태그는 중복이 발생하면 안된다") {
                val response = tagRepository.findAll()
                response.size shouldBe 3
                response.map { it.name } shouldBe listOf("kotlin", "spring", "python")
            }
        }

        `when`("주어진 태그 2개와 미리 작성 되어있던 게시글 1개를 연결 하면") {
            val tags = Tags(
                listOf(Tag("kotlin"), Tag("springboot"))
            )
            tagService.tagMappingWithArticle(tags, article.id)

            then("게시글 태그 2개가 실제 저장되어야 한다") {
                articleTagRepository.findByArticleId(article.id).size shouldBe 2
            }
        }
    }
})