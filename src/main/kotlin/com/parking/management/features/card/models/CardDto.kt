package com.parking.management.features.card.models

import com.example.parking.client.api.ClientResponse
import com.parking.management.features.card.Card
import com.parking.management.features.card.CardStatus
import com.parking.management.features.client.Client
import com.parking.management.features.user.models.UserResponse
import jakarta.validation.constraints.*
import java.time.LocalDateTime
import java.util.UUID

/* =========================
   CREATE
   ========================= */

data class CardCreate(

    @field:NotNull(message = "User ID is required")
    val userId: UUID,

    @field:NotBlank(message = "Card number is required")
    val cardNumber: String,

    @field:NotNull(message = "Credit balance is required")
    val creditBalance: Float,

    @field:NotNull(message = "Status is required")
    val status: CardStatus,

    @field:NotNull(message = "Issued at date is required")
    val issuedAt: LocalDateTime,

    @field:NotNull(message = "Expires at date is required")
    val expiresAt: LocalDateTime
)

/* =========================
   UPDATE
   ========================= */

data class CardUpdate(

    val userId: UUID? = null,
    val cardNumber: String? = null,
    val creditBalance: Float? = null,
    val status: CardStatus? = null,
    val issuedAt: LocalDateTime? = null,
    val expiresAt: LocalDateTime? = null
)

/* =========================
   RESPONSE
   ========================= */

data class CardResponse(
    val id: UUID,
    val client: ClientResponse,
    val cardNumber: String,
    val creditBalance: Float,
    val status: CardStatus,
    val issuedAt: LocalDateTime,
    val expiresAt: LocalDateTime,
    val createdAt: LocalDateTime
)

/* =========================
   MAPPER
   ========================= */

object CardMapper {

    fun toEntity(dto: CardCreate, client: Client): Card =
        Card(
            client = client,
            cardNumber = dto.cardNumber,
            creditBalance = dto.creditBalance,
            status = dto.status,
            issuedAt = dto.issuedAt,
            expiresAt = dto.expiresAt
        )

    fun toResponse(entity: Card, clientResponse: ClientResponse): CardResponse =
        CardResponse(
            id = entity.id!!,
            client = clientResponse,
            cardNumber = entity.cardNumber,
            creditBalance = entity.creditBalance,
            status = entity.status,
            issuedAt = entity.issuedAt,
            expiresAt = entity.expiresAt,
            createdAt = entity.createdAt!!
        )
}

fun Card.merge(cardUpdate: CardUpdate) {
    cardUpdate.cardNumber?.let {
        cardNumber = it
    }
    cardUpdate.creditBalance?.let {
        creditBalance = it
    }
    cardUpdate.status?.let {
        status = it
    }
    cardUpdate.issuedAt?.let {
        issuedAt = it
    }
    cardUpdate.expiresAt?.let {
        expiresAt = it
    }
}
