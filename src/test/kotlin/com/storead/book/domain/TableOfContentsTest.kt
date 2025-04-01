package com.storead.book.domain

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate


@SpringBootTest
@ActiveProfiles("test")
@DisplayName("책 목차 도메인 테스트")
class TableOfContentsTest(
    @Autowired val tocRepository: TableOfContentsRepository,
    @Autowired val bookRepository: BookRepository,
) : BehaviorSpec({

    given("책 목차 데이터를 수집 한 경우") {
        val book = bookRepository.save(Book("12345", "JPA", "test", "test", LocalDate.now()))
        val toc = listOf("1장. JPA 소개", "2장. JPA 시작")
        val rawToc = RawTableOfContents(book, toc)
        `when`("배열에 있는 책 목차를 저장하면") {
            val result = tocRepository.saveAll(rawToc.toEntities())
            then("순차적으로 목차 정보가 저장되어야한다") {
                result shouldHaveSize 2
                result.map { it.title } shouldContainExactly listOf("1장. JPA 소개", "2장. JPA 시작")
            }
        }
    }
})