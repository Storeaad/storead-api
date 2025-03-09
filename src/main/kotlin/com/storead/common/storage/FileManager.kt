package com.storead.common.storage

import org.springframework.web.multipart.MultipartFile


data class File(
    val uri: String,
    val name: String
)


interface FileManager {

    fun saveImage(file: MultipartFile): File

    fun validate(file: MultipartFile): FileManager

}