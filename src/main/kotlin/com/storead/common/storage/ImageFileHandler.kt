package com.storead.common.storage

import com.storead.config.properties.ImageFileProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Component
class ImageFileHandler(
    private val fileProperties: ImageFileProperties,
    @Value("\${spring.static.dir}") private val STATIC_FILES: String,

    ) : FileManager {

    private val IMAGE_LIMIT_SIZE = 10 * 1024 * 1024

    override fun saveImage(file: MultipartFile): File {
        val fileName: String = file.originalFilename!!

        val filePath: Path = Paths.get("$STATIC_FILES/$fileName");
        Files.copy(file.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)

        val imageUrl = "/static/$fileName"
        return File(imageUrl, fileName)
    }

    override fun validate(file: MultipartFile): FileManager {

        val fileSize = file.size
        if (fileSize > IMAGE_LIMIT_SIZE) {
            throw IllegalArgumentException("최대 10MB까지의 이미지만 업로드할 수 있습니다.")
        }

        val imageType = file.contentType
        if (imageType !in fileProperties.contentTypes) {
            throw IllegalArgumentException("지원하지 않는 파일의 형식입니다.")
        }

        val fileName: String? = file.originalFilename
        val fileExtension: String? = fileName?.substringAfterLast('.')?.lowercase()

        if (fileExtension !in fileProperties.extensions) {
            throw IllegalArgumentException("지원하지 않는 파일 확장자입니다.")
        }
        return this
    }
}