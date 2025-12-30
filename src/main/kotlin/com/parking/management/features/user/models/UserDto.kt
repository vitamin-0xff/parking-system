package com.parking.management.features.user.models

import com.parking.management.features.user.User
import com.parking.management.features.user.UserStatus
import jakarta.validation.constraints.*
import java.time.LocalDateTime
import java.util.UUID

/* =========================
   CREATE
   ========================= */

data class UserCreate(

    @field:NotBlank(message = "Full name is required")
    val fullName: String,

    @field:NotBlank(message = "Email is required")
    @field:Email
    val email: String,

    @field:NotBlank(message = "Phone is required")
    val phone: String,

    @field:NotNull(message = "Status is required")
    val status: UserStatus
)

/* =========================
   UPDATE
   ========================= */

data class UserUpdate(

    val fullName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val status: UserStatus? = null
)

/* =========================
   RESPONSE
   ========================= */

data class UserResponse(
    val id: UUID,
    val fullName: String,
    val email: String,
    val phone: String,
    val status: UserStatus,
    val createdAt: LocalDateTime
)

/* =========================
   MAPPER
   ========================= */

object UserMapper {

    fun toEntity(dto: UserCreate): User =
        User(
            fullName = dto.fullName,
            email = dto.email,
            phone = dto.phone,
            status = dto.status
        )

    fun toResponse(entity: User): UserResponse =
        UserResponse(
            id = entity.id!!,
            fullName = entity.fullName,
            email = entity.email,
            phone = entity.phone,
            status = entity.status,
            createdAt = entity.createdAt!!
        )
}

fun User.merge(userUpdate: UserUpdate) {
    userUpdate.fullName?.let {
        fullName = it
    }
    userUpdate.email?.let {
        email = it
    }
    userUpdate.phone?.let {
        phone = it
    }
    userUpdate.status?.let {
        status = it
    }
}
