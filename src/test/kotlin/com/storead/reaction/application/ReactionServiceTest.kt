package com.storead.reaction.application

import com.github.f4b6a3.ulid.UlidCreator
import com.storead.IntegrationTestSupport
import com.storead.common.domain.EntityType
import com.storead.reaction.application.request.ReactionServiceCreateRequest
import com.storead.reaction.domain.ReactionRepository
import com.storead.reaction.domain.ReactionType
import io.kotest.core.spec.DisplayName
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

@DisplayName("반응 서비스 테스트")
class ReactionServiceTest(
    @Autowired private val reactionRepository: ReactionRepository,
    @Autowired private val reactionService: ReactionService,
) : IntegrationTestSupport({

    given("이미 등록된 게시글에 반응을 등록할 때") {
        val articleId = UlidCreator.getMonotonicUlid().toUuid()
        val userId = UlidCreator.getMonotonicUlid().toUuid()
        val request = ReactionServiceCreateRequest(
            userId = userId,
            subjectId = articleId,
            subjectType = EntityType.ARTICLE,
            reactionType = ReactionType.LIKE,
        )
        `when`("반응을 등록하면") {
            reactionService.createReaction(request)

            then("리액션이 정상적으로 등록되어야 한다") {
                val reactions = reactionRepository.findAll()
                reactions.size shouldBe 1
                reactions.first().reactionType shouldBe  ReactionType.LIKE
            }
        }

        `when`("같은 게시글에 같은 유저가 다른 반응을 등록하면") {
            val anotherRequest = ReactionServiceCreateRequest(
                userId = userId,
                subjectId = articleId,
                subjectType = EntityType.ARTICLE,
                reactionType = ReactionType.WOW,
            )
            reactionService.createReaction(anotherRequest)

            then("기존 반응이 업데이트되어야 한다") {
                val reactions = reactionRepository.findAllBySubjectIdAndSubjectType(articleId, EntityType.ARTICLE)
                reactions.size shouldBe 1
                reactions.first().reactionType shouldBe  ReactionType.WOW
            }
        }

        `when`("같은 게시글에 같은 유저가 같은 반응을 등록하면") {
            val sameRequest = ReactionServiceCreateRequest(
                userId = userId,
                subjectId = articleId,
                subjectType = EntityType.ARTICLE,
                reactionType = ReactionType.WOW,
            )
            reactionService.createReaction(sameRequest)

            then("기존 반응이 삭제되어야 한다") {
                val reactions = reactionRepository.findByUserIdAndSubjectId(userId, articleId)
                reactions shouldBe null
            }
        }

    }
})