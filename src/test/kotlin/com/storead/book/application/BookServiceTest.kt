package com.storead.book.application

import com.storead.IntegrationTestSupport
import com.storead.book.domain.Book
import com.storead.book.domain.BookRepository
import com.storead.book.exception.BookException
import com.storead.book.signal.BookCreateEvent
import com.storead.book.web.request.BookCreateRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@DisplayName("책 관리 서비스 테스트")
class BookServiceTest(
    @Autowired private val bookService: BookService,
    @Autowired val bookRepository: BookRepository,
) : IntegrationTestSupport({

    afterSpec {
        bookRepository.deleteAll()
    }

    val eventPublisher = mockk<ApplicationEventPublisher>()

    given("신규 책 등록 요청이 주어진 경우") {
        val request = BookCreateRequest("isbn", "수확자", "author", "20250331", "description", "url")
        val serviceRequest = request.toServiceRequest()
        every { eventPublisher.publishEvent(any<BookCreateEvent>()) } just Runs

        `when`("해당 책 등록 요청을 처리하여 데이터베이스에 저장하면") {
            val response = bookService.create(serviceRequest)
            then("데이터베이스에 저장된 책의 정보가 정확히 반영되어야 한다") {
                bookRepository.findById(response.bookUuid).get().title shouldBe "수확자"
            }
        }
    }

    given("이미 데이터베이스에 저장된 책 정보가 있는 경우") {
        val date = LocalDate.parse("20250331", DateTimeFormatter.ofPattern("yyyyMMdd"))
        val book = bookRepository.save(
            Book("isbn", "수확자", "author", "description", date)
        )
        `when`("저장된 책의 UUID를 사용하여 해당 책 정보를 조회하면") {
            val response = bookService.getByUuid(book.id!!)
            then("저장된 책의 정보가 올바르게 반환되어야 한다") {
                response.title shouldBe "수확자"
            }
        }

        `when`("등록되지 않은 책의 UUID를 입력하면") {
            val randomUUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
            then("등록되지 않은 책이라는 에러가 발생한다") {
                shouldThrow<BookException> {
                    bookService.getByUuid(randomUUID)
                }
            }
        }
    }
})