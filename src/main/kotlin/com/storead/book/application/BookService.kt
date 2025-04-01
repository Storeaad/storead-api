package com.storead.book.application

import com.storead.book.application.request.BookServiceCreateRequest
import com.storead.book.application.response.BookServiceResponse
import com.storead.book.domain.BookRepository
import com.storead.book.exception.BookException
import com.storead.book.signal.BookCreateEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID


@Service
class BookService(
    private val bookRepository: BookRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    fun create(createRequest: BookServiceCreateRequest): BookServiceResponse {
        val book = bookRepository.save(createRequest.toEntity())
        eventPublisher.publishEvent(BookCreateEvent(book))
        return BookServiceResponse(book)
    }

    fun getByUuid(getRequest: UUID): BookServiceResponse {
        val book = bookRepository.findById(getRequest).orElseThrow { BookException("등록되지 않은 책 정보입니다.")}
        return BookServiceResponse(book)
    }
}