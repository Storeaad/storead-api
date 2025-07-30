package com.storead.reaction.domain

import com.storead.common.domain.EntityType
import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.util.UUID

@DisplayName("반응 도메인 테스트")
class ReactionTest: BehaviorSpec({

    given("반응 도메인의 상태를 변경하는 경우") {
        val reaction = Reaction(
            userId = UUID.randomUUID(),
            subjectId = UUID.randomUUID(),
            subjectType = EntityType.ARTICLE,
            reactionType = ReactionType.LIKE
        )
        `when`("기존 반응을 업데이트하는 경우") {

            reaction.updateReactionType(ReactionType.CLAP)

            then("반응 타입이 변경되어야 한다") {
                reaction.reactionType shouldBe ReactionType.CLAP
            }
        }

        `when`("유저, 대상, 반응 정보가 같은 내용으로 반응이 존재하는지 확인하면") {
            val hasAlreadyReacted = reaction.hasAlreadyReacted(
                reaction.userId,
                reaction.subjectId,
                reaction.reactionType
            )
            then("이미 반응한 것으로 판단되어야 한다") {
                hasAlreadyReacted shouldBe true
            }
        }

        `when`("유저, 대상, 반응 정보가 다른 내용으로 반응이 존재하는지 확인하면") {
            val hasAlreadyReacted = reaction.hasAlreadyReacted(
                UUID.randomUUID(),
                UUID.randomUUID(),
                ReactionType.LIKE
            )
            then("아직 반응하지 않은 것으로 판단되어야 한다") {
                hasAlreadyReacted shouldBe false
            }
        }
    }

})