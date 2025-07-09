package com.storead.article.domain

import com.github.f4b6a3.ulid.UlidCreator
import com.storead.profile.domain.Profile
import com.storead.profile.domain.ProfileRepository
import com.storead.tag.domain.Tag
import com.storead.tag.domain.TagRepository
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("게시글 레포지토리 테스트")
class ArticleRepositoryTest(
    @Autowired private val articleRepository: ArticleRepository,
    @Autowired private val tagRepository: TagRepository,
    @Autowired private val articleTagRepository: ArticleTagRepository,
    @Autowired private val profileRepository: ProfileRepository,

    ) : BehaviorSpec({

    lateinit var userId: UUID
    lateinit var profile: Profile
    lateinit var tag: Tag

    beforeSpec {
        userId = UlidCreator.getMonotonicUlid().toUuid()
        tag = tagRepository.save(Tag("kotlin"))
        profile = profileRepository.save(
            Profile(
                profileName = "testProfile",
                userId = userId
            )
        )
    }


    given("태그가 입력된 게시글이 한 개만 등록 되어 있을 때 단일 게시글의 정보만 활용 하는 경우") {
        val givenArticle = createArticle(profile.id, "title", "body", "description")

        articleRepository.save(givenArticle)

        `when`("제목으로 게시글을 조회하면") {
            val article = articleRepository.findByTitle("title")

            then("제목에 해당하는 게시글이 반환된다") {
                article?.title shouldBe "title"
            }
        }

        `when`("게시글 등록 상태와 게시글 고유 아이디로 조회하면") {
            val article = articleRepository.findByIdAndPublishStatus(givenArticle.id, ArticlePublishStatus.PUBLISHED)

            then("해당하는 게시글이 반환된다") {
                article?.title shouldBe "title"
            }
        }
    }

    given("게시글이 한 개만 등록되어 있을 때 연관된 정보도 함께 접근하는 경우") {
        val givenArticle = Article(
            authorProfileId = profile.id,
            title = "title",
            body = "body",
            description = "description",
        )

        articleTagRepository.save(ArticleTag(givenArticle.id, tag.id))
        articleRepository.save(givenArticle)

        `when`("게시글 고유 아이디로 게시글과 연관된 정보를 같이 조회 하면") {
            val articleDetail = articleRepository.findArticleDetailByArticleId(givenArticle.id)

            then("게시글을 작성한 유저의 프로필 정보와 태그 정보를 포함한 결과 값을 반환한다") {
                articleDetail?.authorProfileName shouldBe "testProfile"
                articleDetail?.tags?.size shouldBe 1
                articleDetail?.tags shouldContainExactlyInAnyOrder listOf("kotlin")
            }
        }
    }

    given("여러개의 게시글이 등록되어 있을 때 연관 정보도 함께 접근 하는 경우") {
        val profile2 = profileRepository.save(
            Profile(
                profileName = "testProfile2",
                userId = UlidCreator.getMonotonicUlid().toUuid()
            )
        )

        val givenArticle1 = createArticle(profile.id, "title1", "body1", "description1")
        val givenArticle2 = createArticle(profile.id, "title2", "body2", "description2")
        val givenArticle3 = createArticle(profile2.id, "title3", "body3", "description3")
        articleRepository.saveAll(listOf(givenArticle1, givenArticle2, givenArticle3))

        `when`("게시글 전체를 조회할 때 연관된 정보도 함께 조회하면") {
            val articles = articleRepository.findAllArticles(limit = 3)

            then("게시글을 작성한 유저의 프로필과 태그 정보를 포함한 전체 결과 값을 반환한다") {
                articles.size shouldBe 3
                articles.map { it.article.title } shouldContainExactlyInAnyOrder listOf("title1", "title2", "title3")
                articles.map { it.authorProfileName } shouldContainExactlyInAnyOrder listOf(
                    "testProfile",
                    "testProfile",
                    "testProfile2",
                )
            }
        }

        `when`("게시글 작성자 아이디를 기준으로 작성한 모든 게시글을 조회 하면") {
            val articles = articleRepository.findAllArticlesByAuthorId(profile.id, limit = 2)

            then("게시글 작성자가 등록 했던 모든 게시글과 연관 정보를 반환한다") {
                articles.size shouldBe 2
                articles.map { it.article.title } shouldContainExactlyInAnyOrder listOf("title1", "title2")
                articles.map { it.authorProfileName } shouldContainExactlyInAnyOrder listOf("testProfile", "testProfile")
            }
        }
    }
}) {
    companion object {
        private fun createArticle(
            authorProfileId: UUID,
            title: String,
            body: String,
            description: String,
            publishStatus: ArticlePublishStatus = ArticlePublishStatus.PUBLISHED,
        ) = Article(authorProfileId, title, body, description, publishStatus)
    }
}