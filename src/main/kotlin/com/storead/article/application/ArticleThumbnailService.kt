package com.storead.article.application

import com.storead.article.domain.ArticleThumbnailImage
import com.storead.article.domain.ArticleThumbnailImageRepository
import com.storead.common.storage.FileManager
import com.storead.common.storage.UploadFile
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class ArticleThumbnailService(
    private val thumbnailRepository: ArticleThumbnailImageRepository,
    private val imageFileHandler: FileManager
) {
    fun upload(thumbnailImage: MultipartFile?): ArticleThumbnailImage? {
        thumbnailImage ?: return null

        val storedFile = imageFileHandler.saveImage(UploadFile(thumbnailImage))

        return thumbnailRepository.save(
            ArticleThumbnailImage(storedFile.uri)
        )

    }
}