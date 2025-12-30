package com.parking.management.comman.models

data class UpdateResponse<T>(
    val message: String,
    val hasChanged: Boolean,
    val data: T
)