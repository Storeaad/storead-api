package com.storead.article.application

import com.storead.IntegrationTestSupport
import com.storead.article.domain.ArticleThumbnailImageRepository
import com.storead.common.storage.FileManager
import com.storead.common.storage.LocalImageFileHandler
import com.storead.common.storage.StoredFile
import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile


class MockFileHandler {
    @Bean
    fun imageFileHandler(): FileManager = mockk<LocalImageFileHandler>(relaxed = true)
}


@Import(MockFileHandler::class)
@DisplayName("게시글 썸네일 서비스 테스트")
class ArticleThumbnailServiceTest(
    @Autowired private val imageFileHandler: FileManager,
    @Autowired private val articleThumbnailService: ArticleThumbnailService,
    @Autowired private val articleThumbnailImageRepository: ArticleThumbnailImageRepository
) : IntegrationTestSupport({

    given("게시글에 썸네일 이미지가 들어오는 경우") {
        val multipartFile: MultipartFile = MockMultipartFile(
            "file",
            "test-thumbnail.png",
            "image/png",
            "이미지 내용".toByteArray()
        )

        every { imageFileHandler.saveImage(any()) } returns StoredFile(
            "/static/test-thumbnail.png",
            "test-thumbnail.png"
        )

        `when`("업로드 요청을 하면") {
            val response = articleThumbnailService.upload(multipartFile)!!

            then("DB에 저장된 썸네일 URL이 반환값과 일치해야 한다") {
                val saved = articleThumbnailImageRepository.findById(response.id).get()
                saved.thumbnailUrl shouldBe response.thumbnailUrl
            }
        }
    }

    given("게시글 섬네일을 입력하지 않는 경우") {
        val thumbnail = null

        `when`("섬네일 없이 섬네일을 업로드 하면") {
            val response = articleThumbnailService.upload(thumbnail)

            then("null 이 반환 되어야 한다") {
                response shouldBe null
            }
        }
    }
})