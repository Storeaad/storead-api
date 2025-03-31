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

    @PostMapping
    fun create(request: BookCreateRequest): ResponseEntity<ApiResponse<BookResponse>> {
        val response: BookResponse = bookService.create(request.toServiceRequest()).toResponse()
        return ApiResponse.success(data = response, "${response.title} 도서가 정상적으로 등록 되었습니다.", HttpStatus.CREATED)
    }

    @GetMapping("/{book}")
    fun getBook(
        @PathVariable book: UUID,
    ): ResponseEntity<ApiResponse<BookResponse>> {
        val response: BookResponse = bookService.getByUuid(book).toResponse()
        return ApiResponse.success(data = response, message = "요청하신 책이 정상적으로 조회 되었습니다.")
    }
}