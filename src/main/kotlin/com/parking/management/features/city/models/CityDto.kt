package com.parking.management.features.city.models

import com.parking.management.features.city.City
import com.parking.management.features.country.models.CountryResponse
import jakarta.validation.constraints.*
import java.time.LocalDateTime
import java.util.UUID

/* =========================
   CREATE
   ========================= */

data class CityCreate(

    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "Postal code is required")
    val postalCode: String,

    @field:NotBlank(message = "State code is required")
    val stateCode: String,

    @field:NotNull(message = "Country ID is required")
    val countryId: UUID
)

/* =========================
   UPDATE
   ========================= */

data class CityUpdate(

    val name: String? = null,
    val postalCode: String? = null,
    val stateCode: String? = null,
    val countryId: UUID? = null
)

/* =========================
   RESPONSE
   ========================= */

data class CityResponse(
    val id: UUID,
    val name: String,
    val postalCode: String,
    val stateCode: String,
    val country: CountryResponse,
    val createdAt: LocalDateTime
)

/* =========================
   MAPPER
   ========================= */

object CityMapper {

    fun toResponse(entity: City): CityResponse =
        CityResponse(
            id = entity.id!!,
            name = entity.name,
            postalCode = entity.postalCode,
            stateCode = entity.stateCode,
            country = com.parking.management.features.country.models.CountryMapper.toResponse(entity.country),
            createdAt = entity.createdAt!!
        )
}

fun City.merge(cityUpdate: CityUpdate) {
    cityUpdate.name?.let {
        name = it
    }
    cityUpdate.postalCode?.let {
        postalCode = it
    }
    cityUpdate.stateCode?.let {
        stateCode = it
    }
}
