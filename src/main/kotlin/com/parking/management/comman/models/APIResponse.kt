package com.parking.management.comman.models

import java.time.LocalDateTime


data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ApiError? = null,
    val timestamp: String = LocalDateTime.now().toString(),
    val status: Int,
    val path: String
) {
    companion object {
        fun <T> success(data: T, status: Int = 200, path: String) =
            ApiResponse(success = true, data = data, status = status, path = path)

        fun error(error: ApiError, status: Int, path: String) =
            ApiResponse<Nothing>(success = false, error = error, status = status, path = path)
    }
}

data class ApiError(
    val code: String,
    val message: String,
    val details: List<String>? = null
)