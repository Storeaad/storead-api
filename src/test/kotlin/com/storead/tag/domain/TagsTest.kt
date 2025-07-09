package com.storead.tag.domain

import com.github.f4b6a3.ulid.UlidCreator
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.springframework.test.context.ActiveProfiles


@ActiveProfiles("test")
@DisplayName("여러 태그들을 관리하는 도메인 테스트")
class TagsTest(
): BehaviorSpec({
    given("유저가 게시글을 작성 또는 업데이트 하는 경우") {
        val articleId = UlidCreator.getMonotonicUlid().toUuid()
        val kotlinTag = Tag("kotlin")
        val springBootTag = Tag("spring boot")
        val tags = Tags(listOf(kotlinTag, springBootTag))

        `when`("입력한 태그들을 리스트로 반환하면") {
            val result = tags.asList()

            then("태그 전체가 반환된다") {
                result.size shouldBe 2
                result.map { it.name }.containsAll(listOf("kotlin", "spring boot"))
            }
        }

        `when`("이미 작성된 태그에서 새로운 태그를 입력할 때 중복으로 입력 하면") {
            val newTags = tags.createNewTagsFrom(Tags(listOf(Tag("kotlin"))))

            then("아무것도 반환되지 않아야한다") {
                newTags.asList().shouldBeEmpty()
            }
        }

        `when`("이미 작성된 태그에서 새로운 태그를 입력하면") {
            val newTags = tags.createNewTagsFrom(Tags(listOf(Tag("django"))))

            then("중복된 태그를 제외하고 새롭게 추가된 태그만 반환해야한다") {
                newTags.asList().size shouldBe 1
                newTags.names() shouldContainExactlyInAnyOrder listOf("django")
            }
        }

        `when`("기존에 작성된 태그와 신규로 입력한 태그를 합치면") {
            val newTags = tags.createNewTagsFrom(Tags(listOf(Tag("django"))))
            val result = newTags.extend(tags)

            then("입력한 모든 태그가 반환되어야한다") {
                result.asList().size shouldBe 3
                result.names() shouldContainExactlyInAnyOrder listOf("kotlin", "spring boot", "django")
            }
        }

        `when`("태그를 아티클과 연결하면") {
            val articleTag = tags.toArticleTags(articleId)

            then("한 개의 게시글에 여러 개의 태그가 연결된다") {
                articleTag.size shouldBe 2
                articleTag.map { it.tagId to it.articleId } shouldContainExactlyInAnyOrder listOf(
                    kotlinTag.id to articleId,
                    springBootTag.id to articleId
                )
            }
        }
    }
})