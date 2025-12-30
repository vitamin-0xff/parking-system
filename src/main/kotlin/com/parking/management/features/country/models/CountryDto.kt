package com.parking.management.features.country.models

import com.parking.management.features.country.Country
import jakarta.validation.constraints.*
import java.time.LocalDateTime
import java.util.UUID

/* =========================
   CREATE
   ========================= */

data class CountryCreate(

    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "ISO code is required")
    val isoCode: String
)

/* =========================
   UPDATE
   ========================= */

data class CountryUpdate(

    val name: String? = null,
    val isoCode: String? = null
)

/* =========================
   RESPONSE
   ========================= */

data class CountryResponse(
    val id: UUID,
    val name: String,
    val isoCode: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

/* =========================
   MAPPER
   ========================= */

object CountryMapper {

    fun toEntity(dto: CountryCreate): Country =
        Country(
            name = dto.name,
            isoCode = dto.isoCode
        )

    fun toResponse(entity: Country): CountryResponse =
        CountryResponse(
            id = entity.id!!,
            name = entity.name,
            isoCode = entity.isoCode,
            createdAt = entity.createdAt!!,
            updatedAt = entity.updateAt!!
        )
}

fun Country.merge(countryUpdate: CountryUpdate) {
    countryUpdate.name?.let {
        name = it
    }
    countryUpdate.isoCode?.let {
        isoCode = it
    }
}
