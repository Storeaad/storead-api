package com.storead.book.application

import com.storead.book.domain.Book
import com.storead.book.domain.BookIsbn
import com.storead.book.domain.RawTableOfContents
import com.storead.book.domain.TableOfContentsRepository
import com.storead.book.signal.BookCreateEvent
import com.storead.common.web.AwsLambdaHandler
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service


@Service
class BookTableOfContentsService(
    private val awsLambdaHandler: AwsLambdaHandler,
    private val tocRepository: TableOfContentsRepository,
) {

    private val lambdaFunctionName = "book-toc-crawler"

    @Async
    @EventListener
    fun bookCreateEventListener(event: BookCreateEvent) {
        val dataIntegration = integration(event.instance)
        create(dataIntegration)
    }

    private fun create(rawTableOfContents: RawTableOfContents) {
        tocRepository.saveAll(rawTableOfContents.toEntities())
    }

    private fun integration(book: Book): RawTableOfContents {
        val bookRequest = BookIsbn(book.isbn)
        val payloadJson = Json.encodeToString(bookRequest)

        val response = awsLambdaHandler.call(lambdaFunctionName, payloadJson)

        return RawTableOfContents(book, response.data)
    }
}