package com.storead.book.web

import com.storead.book.application.BookService
import com.storead.book.web.request.BookCreateRequest
import com.storead.book.web.response.BookResponse
import com.storead.common.web.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID


@RestController
@RequestMapping("/api/v1/books")
class BookController(
    private val bookService: BookService,
) {

    /**
     * 책 정보 등록
     *
     * 책 정보를 입력할 시 목차 정보를 수집 하고 책 정보를 등록 함.
     *
     * @param request 등록할 책의 정보(isbn, title, author, publishedDate, description, thumbnailUrl)
     * @return 책 정보 등록 결과
     */
    @PostMapping
    fun create(request: BookCreateRequest): ResponseEntity<ApiResponse<BookResponse>> {
        val response: BookResponse = bookService.create(request.toServiceRequest()).toResponse()
        return ApiResponse.success(data = response, "${response.title} 도서가 정상적으로 등록 되었습니다.", HttpStatus.CREATED)
    }

    /**
     * 책 조회
     *
     * 책의 고유한 아이디를 입력 하면 해당 책의 정보를 반환 함
     *
     * @param book: 책의 UUID 36자리
     * @return 책 정보 조회 결과
     */
    @GetMapping("/{book}")
    fun getBook(
        @PathVariable book: UUID,
    ): ResponseEntity<ApiResponse<BookResponse>> {
        val response: BookResponse = bookService.getByUuid(book).toResponse()
        return ApiResponse.success(data = response, message = "요청하신 책이 정상적으로 조회 되었습니다.")
    }
}