package com.parking.management.features.credit_suppling.models

import com.parking.management.features.card.Card
import com.parking.management.features.card.models.CardMapper
import com.parking.management.features.card.models.CardResponse
import com.parking.management.features.credit_suppling.CreditSuppling
import com.parking.management.features.credit_suppling.CreditSupplingSource
import com.parking.management.features.credit_suppling.CreditSupplingStatus
import com.example.parking.client.api.ClientMapper
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.UUID

/* =========================
   CREATE
   ========================= */

data class CreditSupplingCreate(
    @field:NotNull(message = "Card ID is required")
    val cardId: UUID,

    @field:NotNull(message = "Amount is required")
    val amount: Float,

    @field:NotNull(message = "Fee taken is required")
    val feeTaken: Float,

    @field:NotNull(message = "Source is required")
    val source: CreditSupplingSource,

    @field:NotNull(message = "Status is required")
    val status: CreditSupplingStatus,

    @field:NotNull(message = "Reference is required")
    val reference: String
)

/* =========================
   RESPONSE
   ========================= */

data class CreditSupplingResponse(
    val id: UUID,
    val card: CardResponse,
    val amount: Float,
    val feeTaken: Float,
    val source: CreditSupplingSource,
    val status: CreditSupplingStatus,
    val balanceBefore: Float,
    val balanceAfter: Float,
    val reference: String,
    val createdAt: LocalDateTime
)

/* =========================
   MAPPER
   ========================= */

object CreditSupplingMapper {
    fun toEntity(dto: CreditSupplingCreate, card: Card, balanceBefore: Float, balanceAfter: Float): CreditSuppling =
        CreditSuppling(
            card = card,
            amount = dto.amount,
            feeTaken = dto.feeTaken,
            source = dto.source,
            status = dto.status,
            balanceBefore = balanceBefore,
            balanceAfter = balanceAfter,
            reference = dto.reference
        )

    fun toResponse(entity: CreditSuppling): CreditSupplingResponse {
        val clientResponse = ClientMapper.toResponse(entity.card.client)
        val cardResponse = CardMapper.toResponse(entity.card, clientResponse)
        return CreditSupplingResponse(
            id = entity.id!!,
            card = cardResponse,
            amount = entity.amount,
            feeTaken = entity.feeTaken,
            source = entity.source,
            status = entity.status,
            balanceBefore = entity.balanceBefore,
            balanceAfter = entity.balanceAfter,
            reference = entity.reference,
            createdAt = entity.createdAt!!
        )
    }
}
