package com.storead.reaction.domain

import com.github.f4b6a3.ulid.UlidCreator
import com.storead.IntegrationTestSupport
import com.storead.common.domain.EntityType.ARTICLE
import com.storead.reaction.domain.ReactionType.*
import io.kotest.core.spec.DisplayName
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@DisplayName("반응 레포지토리 테스트")
class ReactionRepositoryTest(
    @Autowired private val reactionRepository: ReactionRepository,
) : IntegrationTestSupport({

    fun createReactionWithLikeToArticle(
        userId: UUID,
        subjectId: UUID,
        reactionType: ReactionType = LIKE,
    ): Reaction {
        val reaction = Reaction(
            userId = userId,
            subjectId = subjectId,
            subjectType = ARTICLE,
            reactionType = reactionType
        )
        return reactionRepository.save(reaction)
    }

    given("사용자가 이미 존재하는 게시글에 반응을 남긴 경우") {
        val userId1 = UlidCreator.getMonotonicUlid().toUuid()
        val userId2 = UlidCreator.getMonotonicUlid().toUuid()
        val userId3 = UlidCreator.getMonotonicUlid().toUuid()

        val articleId = UlidCreator.getMonotonicUlid().toUuid()

        createReactionWithLikeToArticle(userId = userId1, subjectId = articleId)
        createReactionWithLikeToArticle(userId = userId2, subjectId = articleId, reactionType = LOVE)
        createReactionWithLikeToArticle(userId = userId3, subjectId = articleId, reactionType = WOW)

        `when`("특정 사용자가 남긴 반응을 모두 조회하면") {
            val reactions = reactionRepository.findAllByUserId(userId1)

            then("해당 유저가 남긴 반응만 반환되어야 한다") {
                reactions.size shouldBe 1
                reactions.first().userId shouldBe userId1
                reactions.first().subjectId shouldBe articleId
                reactions.first().subjectType shouldBe ARTICLE
                reactions.first().reactionType shouldBe LIKE
            }
        }

        `when`("특정 게시글에 달린 모든 반응을 조회하면") {
            val reactions = reactionRepository.findAllBySubjectIdAndSubjectType(articleId, ARTICLE)

            then("해당 게시글에 대한 모든 반응이 반환되어야 한다") {
                reactions.size shouldBe 3
                reactions.map { it.userId } shouldContainExactlyInAnyOrder  listOf(userId1, userId2, userId3)
                reactions.map { it.reactionType } shouldContainExactlyInAnyOrder  listOf(LIKE, LOVE, WOW)
            }
        }

        `when`("특정 게시글에 대한 반응을 삭제하면") {
            reactionRepository.deleteAllBySubjectIdAndSubjectType(articleId, ARTICLE)

            then("해당 게시글에 대한 모든 반응이 삭제되어야 한다") {
                val reactionsAfterDelete = reactionRepository.findAllBySubjectIdAndSubjectType(articleId, ARTICLE)
                reactionsAfterDelete shouldBe emptyList()
            }
        }
    }

    given("특정 게시글에 대한 반응이 없는 경우") {
        val userId4 = UlidCreator.getMonotonicUlid().toUuid()
        val nonExistentArticleId = UlidCreator.getMonotonicUlid().toUuid()

        `when`("특정 사용자가 반응을 남기지 않은 게시글에 대해 조회하면") {
            val reactions = reactionRepository.findAllByUserId(userId4)

            then("해당 유저가 남긴 반응이 없으므로 빈 리스트가 반환되어야 한다") {
                reactions shouldBe emptyList()
            }
        }

        `when`("반응이 없는 게시글에 대해 조회하면") {
            val reactions = reactionRepository.findAllBySubjectIdAndSubjectType(nonExistentArticleId, ARTICLE)

            then("빈 리스트가 반환되어야 한다") {
                reactions shouldBe emptyList()
            }
        }
    }
})