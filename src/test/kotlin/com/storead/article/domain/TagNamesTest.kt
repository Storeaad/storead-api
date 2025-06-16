package com.storead.article.domain

import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.springframework.test.context.ActiveProfiles


@ActiveProfiles("test")
@DisplayName("문자열 태그 명칭 테스트")
class TagNamesTest : BehaviorSpec({
    given("사용자에게 태그를 입력 받는 경우") {
        val tagNames = TagNames(listOf("Kotlin"))

        `when`("첫 글자가 대문자인 태그 명칭을 ") {
            val tags = tagNames.toTags()

            then("소문자 태그 명칭이 반환된다") {
                tags.names() shouldContainExactlyInAnyOrder listOf("kotlin")
            }
        }

        `when`("중복으로 입력한 태그 명칭을 정규화 하면") {
            val duplicateTagNames = TagNames(listOf("spring", "SPRING", "Spring"))
            val tags = duplicateTagNames.toTags()

            then("중복이 제거 되어야한다") {
                tags.asList().size shouldBe 1
                tags.names() shouldContainExactlyInAnyOrder listOf("spring")
            }
        }

        `when`("공백이 포함된 태그를 정규화 하면") {
            val duplicateTagNames = TagNames(listOf("spring", "kotlin", "", " "))
            val tags = duplicateTagNames.toTags()

            then("공백은 제거 된 태그만 반환 되어야한다") {
                tags.asList().size shouldBe 2
                tags.names() shouldContainExactlyInAnyOrder listOf("spring", "kotlin")
            }
        }

    }
})