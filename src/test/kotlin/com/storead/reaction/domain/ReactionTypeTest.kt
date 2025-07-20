package com.storead.reaction.domain

import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe


@DisplayName("반응 타입 테스트")
class ReactionTypeTest : BehaviorSpec({
    given("문자열로부터 반응을 생성 하는 경우") {
        val likeTypeStrings = listOf("Like", "like", "LIKE", "LiKe", "like ", " like")

        `when`("대소문자가 뒤섞인 문자열로 반응을 생성하면") {
            then("대소문자를 구분하지 않고 정확한 반응 Enum 값을 반환한다") {
                likeTypeStrings.forEach { typeString ->
                    val result = ReactionType.from(typeString)
                    result shouldBe ReactionType.LIKE
                }
            }
        }

        `when`("존재하지 않는 문자열로 반응을 생성하면") {
            then("null을 반환한다") {
                val invalidTypeString = "invalid"
                val result = ReactionType.from(invalidTypeString)
                result shouldBe null
            }
        }

        `when`("빈 문자열로 반응을 생성하면") {
            then("null을 반환한다") {
                val emptyTypeString = ""
                val result = ReactionType.from(emptyTypeString)
                result shouldBe null
            }
        }
    }
})