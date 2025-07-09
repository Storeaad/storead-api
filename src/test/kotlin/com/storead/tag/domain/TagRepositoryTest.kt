package com.storead.tag.domain

import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@ActiveProfiles("test")
@SpringBootTest
@DisplayName("태그 레포지토리 테스트")
class TagRepositoryTest(

    @Autowired private val tagRepository: TagRepository,

    ) : BehaviorSpec({

    beforeSpec {
        tagRepository.deleteAllInBatch()
    }

    afterTest {
        tagRepository.deleteAllInBatch()
    }

    given("여러 개의 태그가 모두 등록 되어 있는 경우") {
        val tags = listOf(
            Tag("kotlin"),
            Tag("springboot"),
            Tag("java")
        )
        tagRepository.saveAll(tags)

        `when`("태그 목록을 기준으로 조회하면") {
            val result = tagRepository.findByNameIn(tags.map { it.name })
            then("현재 저장되어있는 태그 목록을 반환한다") {
                result.size shouldBe 3
                result.map { it.name } shouldContainExactlyInAnyOrder listOf("kotlin", "java", "springboot")
            }
        }
    }

    given("여러개의 태그 중 일부만 등록 되어 있는 경우") {
        val tags = listOf(
            Tag("kotlin"),
            Tag("springboot"),
            Tag("java")
        )
        tagRepository.saveAll(tags.slice(0..1))

        `when`("태그 목록으로 조회하면") {
            val result = tagRepository.findByNameIn(tags.map { it.name })
            then("현재 저장 되어있는 태그만 반환된다") {
                result.size shouldBe 2
                result.map { it.name } shouldContainExactlyInAnyOrder listOf("kotlin", "springboot")
            }
        }
    }

})