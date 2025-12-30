package com.example.parking.client.api

import com.parking.management.features.client.Client
import com.parking.management.features.client.ClientStatus
import jakarta.validation.constraints.*
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

/* =========================
   CREATE
   ========================= */

data class ClientCreate(

    @field:NotBlank(message = "Full name is required")
    @field:Size(min = 3, max = 150)
    val fullName: String,

    @field:NotBlank
    @field:Size(min = 2, max = 50)
    val name: String,

    @field:NotBlank
    @field:Size(min = 2, max = 50)
    val lastName: String,

    @field:Email
    val email: String? = null,

    @field:NotBlank
    @field:Pattern(
        regexp = "^[0-9+]{8,15}$",
        message = "Invalid phone number"
    )
    val phone: String
)

/* =========================
   UPDATE
   ========================= */

data class ClientUpdate(

    @field:Size(min = 3, max = 150)
    val fullName: String? = null,

    @field:Size(min = 2, max = 50)
    val name: String? = null,

    @field:Size(min = 2, max = 50)
    val lastName: String? = null,

    @field:Email
    val email: String? = null,

    @field:Pattern(
        regexp = "^[0-9+]{8,15}$",
        message = "Invalid phone number"
    )
    val phone: String? = null
)

/* =========================
   RESPONSE
   ========================= */

data class ClientResponse(
    val id: UUID,
    val fullName: String,
    val name: String,
    val lastName: String,
    val email: String?,
    val phone: String,
    val status: ClientStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

/* =========================
   MAPPER
   ========================= */

object ClientMapper {

    fun toEntity(dto: ClientCreate): Client =
        Client(
            fullName = dto.fullName,
            name = dto.name,
            lastName = dto.lastName,
            email = dto.email,
            phone = dto.phone,
        )

    fun toResponse(entity: Client): ClientResponse =
        ClientResponse(
            id = entity.id!!,
            fullName = entity.fullName,
            name = entity.name,
            lastName = entity.lastName,
            email = entity.email,
            phone = entity.phone,
            status = entity.status,
            createdAt = entity.createdAt!!,
            updatedAt = entity.updatedAt!!
        )
}

fun Client.merge(clientUpdate: ClientUpdate) {
    clientUpdate.name?.let {
        name = it
    }
    clientUpdate.fullName?.let {
        name = it
    }
    clientUpdate.email?.let {
        name = it
    }
    clientUpdate.lastName?.let {
        name = it
    }
    clientUpdate.phone?.let {
        name = it
    }
}
