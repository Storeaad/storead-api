package com.storead.article.application

import com.github.f4b6a3.ulid.UlidCreator
import com.storead.article.application.request.*
import com.storead.article.domain.*
import com.storead.article.exception.ArticleException
import com.storead.profile.domain.Profile
import com.storead.profile.domain.ProfileRepository
import com.storead.tag.domain.TagNames
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode.Root
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@SpringBootTest
@ActiveProfiles("test")
@DisplayName("게시글 서비스 테스트")
class ArticleServiceTest(
    @Autowired private val articleService: ArticleService,
    @Autowired private val articleRepository: ArticleRepository,
    @Autowired private val profileRepository: ProfileRepository,
    @Autowired private val articleViewRepository: ArticleViewRepository,
) : BehaviorSpec({

    extensions(SpringTestExtension(Root))

    lateinit var testProfile: Profile

    beforeSpec {
        val userId = UlidCreator.getMonotonicUlid().toUuid()
        testProfile = profileRepository.save(Profile(userId = userId, profileName = "testProfile"))
    }

    afterTest {
        articleRepository.deleteAllInBatch()
    }

    afterSpec {
        profileRepository.deleteAllInBatch()
        articleViewRepository.deleteAllInBatch()
    }

    given("인증된 사용자가 게시글을 등록하기 위해 요청한 정보가 정상적으로 전달 되었을 때") {
        val request = ArticleCreateServiceRequest(
            userId = testProfile.id,
            title = "testArticle",
            description = "testDescription",
            body = "testBody",
            tags = TagNames(listOf("python")),
        )

        `when`("신규 게시글을 등록하면") {
            val articleResponse = articleService.createArticle(request)

            then("해당 정보로 새 게시글과 기본 조회수가 생성되어야 한다") {
                articleRepository.findByTitle(request.title)?.title shouldBe "testArticle"
                articleViewRepository.findByArticleId(articleResponse.articleId)?.count shouldBe 0
            }
        }
    }

    given("인증된 사용자가 게시글을 수정하기 위해 요청한 정보가 정상적으로 전달 되었을 때") {
        val request = ArticleCreateServiceRequest(
            userId = testProfile.id,
            title = "testArticle",
            description = "testDescription",
            body = "testBody",
            tags = TagNames(listOf("python")),
        )
        articleRepository.save(request.toEntity())

        `when`("등록 되어있는 자신의 게시글 제목을 수정하면") {
            val article = articleRepository.findByTitle(request.title)!!
            val updateRequest = ArticleUpdateServiceRequest(article.id, request.userId, title = "testArticleUpdated")
            articleService.updateArticle(updateRequest)

            then("등록된 게시글의 제목이 수정되어야 한다") {
                articleRepository.findAll().size shouldBe 1
                articleRepository.findByTitle("testArticleUpdated")?.title shouldBe "testArticleUpdated"
            }
        }
    }

    given("인증된 사용자가 게시글을 삭제하기 위해 요청한 정보가 정상적으로 전달 되었을 때") {
        val request = ArticleCreateServiceRequest(
            userId = testProfile.id,
            title = "testArticleUpdated",
            description = "testDescription",
            body = "testBody",
            tags = TagNames(listOf("python")),
        )
        articleRepository.save(request.toEntity())

        `when`("등록 되어있는 자신의 게시글을 삭제하면") {
            val article = articleRepository.findByTitle("testArticleUpdated")!!
            articleService.deleteArticle(ArticleDeleteServiceRequest(article.id, request.userId))

            then("저장된 게시글이 삭제 상태로 변경 되어야한다") {
                articleRepository.findByIdAndPublishStatus(
                    article.id,
                    ArticlePublishStatus.DELETED
                )?.title shouldBe "testArticleUpdated"
            }

            then("게시글과 연관 되어있던 정보들도 삭제되어야한다.") {
                articleViewRepository.findByArticleId(article.id) shouldBe null
                articleTagRepository.findByArticleId(article.id).shouldBeEmpty()
                articleThumbnailImageRepository.findById(thumbnailImageId).getOrNull() shouldBe null
            }
        }
    }

    given("게시글을 작성하지 않은 유저가 있을 때") {
        val article = articleRepository.save(
            Article(testProfile.id, "testArticle", "desc", "body", ArticlePublishStatus.PUBLISHED)
        )
        val request = ArticlesPageServiceRequest(authorId = UlidCreator.getMonotonicUlid().toUuid())

        `when`("해당 유저의 게시글 목록을 조회 하면") {
            val response = articleService.getMyArticles(request)

            then("게시글 목록이 비어있어야 한다") {
                response.articles.shouldBeEmpty()
            }
        }

        `when`("게시글을 수정할 수 없는 유저가 수정 요청을 보내면") {
            val article = articleRepository.save(
                Article(testProfile.id, "testArticle", "desc", "body", ArticlePublishStatus.PUBLISHED)
            )

            val request = ArticleUpdateServiceRequest(article.id, UlidCreator.getMonotonicUlid().toUuid())
            val exception = shouldThrow<ArticleException> { articleService.updateArticle(request) }

            then("작성자가 아니기 때문에 게시글을 수정할 수 없다는 예외가 발생한다") {
                exception.message shouldBe "해당 게시글의 작성자가 아닙니다."
            }
        }
    }

    given("게시글을 작성한 사용자가 있을 때") {
        articleRepository.save(
            Article(testProfile.id, "testArticle", "desc", "body", ArticlePublishStatus.PUBLISHED)
        )

        `when`("해당 사용자가 자신의 게시글 목록을 조회하면") {
            val request = ArticlesPageServiceRequest(limit = 10, authorId = testProfile.id)
            val response = articleService.getMyArticles(request)

            then("자신이 작성한 게시글들이 반환되어야 한다") {
                response.articles.size shouldBe 1
                response.articles.first().authorProfileId shouldBe testProfile.id
            }
        }

        `when`("작성자 정보 없이 게시글 목록을 요청하면") {
            val exception =
                shouldThrow<ArticleException> { articleService.getMyArticles(ArticlesPageServiceRequest()) }
            then("도메인 정책상 작성자 식별자는 필수이며, 예외가 발생해야 한다") {
                exception.message shouldBe "게시글 작성자의 아이디 값이 비어있습니다."
            }
        }
    }

    given("여러 개의 게시글이 게시된 상태에서 특정 게시글만 조회 하는 경우") {
        val articlesView = mutableListOf<ArticleView>()
        val articles = (1..6).map {
            Article(
                authorProfileId = testProfile.id,
                title = "testArticle$it",
                description = "desc$it",
                body = "body$it",
                publishStatus = ArticlePublishStatus.PUBLISHED
            )
        }.also {
            articleRepository.saveAll(it)
        }

        articles.map { articlesView.add(ArticleView(it.id)) }
            .also { articleViewRepository.saveAll(articlesView) }

        `when`("특정 게시글을 상세 조회 하면") {
            val request = ArticleDetailServiceRequest(articles.first().id)
            val response = articleService.getArticleDetail(request)

            then("해당 게시글에 대한 정보가 반환되어야 한다") {
                with(response) {
                    articleId shouldBe articles.first().id
                    authorProfileId shouldBe testProfile.id
                    title shouldBe "testArticle1"
                    authorName shouldBe "testProfile"
                }
            }
        }
    }

    given("여러 개의 게시글이 게시된 상태에서 최신 5개의 게시글만 조회 하는 경우") {
        val articlesView = mutableListOf<ArticleView>()
        val articles = (1..6).map {
            Article(
                authorProfileId = testProfile.id,
                title = "testArticle$it",
                description = "desc$it",
                body = "body$it",
                publishStatus = ArticlePublishStatus.PUBLISHED
            )
        }.also {
            articleRepository.saveAll(it)
        }

        articles.map { articlesView.add(ArticleView(it.id)) }
            .also { articleViewRepository.saveAll(articlesView) }


        `when`("등록 되어 있는 게시글의 최신 5개만 조회 하면") {
            val request = ArticlesPageServiceRequest(limit = 5)
            val response = articleService.getAllArticles(request)

            then("최신 등록된 게시글부터 5개가 반환 되어야한다") {
                response.articles.size shouldBe 5
                val titles = response.articles.map { it.title }
                titles shouldContainAll listOf(
                    "testArticle2",
                    "testArticle3",
                    "testArticle4",
                    "testArticle5",
                    "testArticle6"
                )
            }
        }
    }

    given("여러 개의 게시글이 게시된 상태에서 커서를 이용하여 다음 페이지의 게시글을 조회 하는 경우") {
        val articlesView = mutableListOf<ArticleView>()
        val articles = (1..6).map {
            Article(
                authorProfileId = testProfile.id,
                title = "testArticle$it",
                description = "desc$it",
                body = "body$it",
                publishStatus = ArticlePublishStatus.PUBLISHED
            )
        }.also {
            articleRepository.saveAll(it)
        }

        articles.map { articlesView.add(ArticleView(it.id)) }
            .also { articleViewRepository.saveAll(articlesView) }


        `when`("등록 되어 있는 게시글에서 커서를 이용하여 다음 게시글을 검색하면") {

            val request = ArticlesPageServiceRequest(cursor = articles[1].id)
            val response = articleService.getAllArticles(request)

            then("다음 페이지에 존재하는 1번 게시글만 반환된다") {
                response.articles.size shouldBe 1
                response.articles.first().title shouldBe "testArticle1"
            }
        }
    }
})