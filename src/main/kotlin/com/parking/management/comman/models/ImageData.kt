package com.parking.management.comman.models


enum class SUPPORTED_IMAGE_EXTENSION {
    JPG, JPEG, PNG, WEBP
}

data class ImageDataDto(
    val name: String,
    val extension: SUPPORTED_IMAGE_EXTENSION,
    val imageDataBase64: String
)
