package com.parking.management.features.credit_fee_factor.models

import com.parking.management.features.credit_fee_factor.CreditFeeFactor
import com.parking.management.features.credit_fee_factor.CreditUnit
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.UUID

/* =========================
   CREATE
   ========================= */

data class CreditFeeFactorCreate(
    @field:NotNull(message = "Price is required")
    val price: Float,

    @field:NotNull(message = "Unit is required")
    val unit: CreditUnit
)

/* =========================
   UPDATE
   ========================= */

data class CreditFeeFactorUpdate(
    val price: Float? = null
)

/* =========================
   RESPONSE
   ========================= */

data class CreditFeeFactorResponse(
    val id: UUID,
    val price: Float,
    val unit: CreditUnit,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

/* =========================
   MAPPER
   ========================= */

object CreditFeeFactorMapper {
    fun toEntity(dto: CreditFeeFactorCreate): CreditFeeFactor =
        CreditFeeFactor(
            price = dto.price,
            unit = dto.unit
        )

    fun toResponse(entity: CreditFeeFactor): CreditFeeFactorResponse =
        CreditFeeFactorResponse(
            id = entity.id!!,
            price = entity.price,
            unit = entity.unit,
            createdAt = entity.createdAt!!,
            updatedAt = entity.updatedAt!!
        )
}

fun CreditFeeFactor.merge(dto: CreditFeeFactorUpdate) {
    dto.price?.let {
        this.price = it
    }
}
