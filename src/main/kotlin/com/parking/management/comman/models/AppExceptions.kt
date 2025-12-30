package com.parking.management.comman.models

open class AppException(
    val status: Int,
    val errorCode: String,
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message ?: errorCode, cause)

class NotFoundException(
    message: String = "Resource not found",
    errorCode: String = "NOT_FOUND"
) : AppException(404, errorCode, message)

class BadRequestException(
    message: String = "Bad request",
    errorCode: String = "BAD_REQUEST"
) : AppException(400, errorCode, message)

class UnauthorizedException(
    message: String = "Unauthorized",
    errorCode: String = "UNAUTHORIZED"
) : AppException(401, errorCode, message)

class ForbiddenException(
    message: String = "Forbidden",
    errorCode: String = "FORBIDDEN"
) : AppException(403, errorCode, message)

class ConflictException(
    message: String = "Conflict",
    errorCode: String = "CONFLICT"
) : AppException(409, errorCode, message)

class UnprocessableEntityException(
    val errors: Map<String, String>? = null,
    message: String = "Validation failed",
    errorCode: String = "UNPROCESSABLE_ENTITY"
) : AppException(422, errorCode, message)

class ThirdPartyServiceException(
    message: String,
    serviceName: String
): AppException(
    502,
    "THIRD_PARTY_SERVICE_ERROR",
    "$serviceName: $message"
)