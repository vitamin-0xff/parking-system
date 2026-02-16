package com.parking.management.features.city.models

import com.parking.management.features.city.City
import com.parking.management.features.country.models.CountryResponse
import com.parking.management.specifications.DateRangeFilter
import com.parking.management.specifications.RangeFilter
import com.parking.management.specifications.StringListFilter
import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.Range
import java.time.LocalDateTime
import java.util.UUID

/* =========================
   CREATE
   ========================= */

data class CityCreate(

    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "State code is required")
    val stateCode: String,

    val latitude: Double,

    val longitude: Double,

    @field:Range(min = 0, max = 25, message = "Zoom factor must be between 1 and 25")
    val zoomFactor: Int,

    @field:NotNull(message = "Country ID is required")
    val countryId: UUID
)

/* =========================
   UPDATE
   ========================= */

data class CityUpdate(
    val name: String? = null,
    val stateCode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val zoomFactor: Int? = null,
    val countryId: UUID? = null
)

/* =========================
   RESPONSE
   ========================= */

data class CityResponse(
    val id: UUID,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val zoomFactor: Int,
    val stateCode: String,
    val country: CountryResponse,
    val createdAt: LocalDateTime
)

/* =========================
   SPECIFICATIONS DTO
   ========================= */

data class CitySpecificationsDto(
    val names: StringListFilter? = null,
    val stateCodes: StringListFilter? = null,
    val countryNames: StringListFilter? = null,
    val zoomFactor: RangeFilter? = null,
    val createdAt: DateRangeFilter? = null
)

/* =========================
    MAPPER
    ========================= */

object CityMapper {
    fun toResponse(entity: City): CityResponse =
        CityResponse(
            id = entity.id!!,
            name = entity.name,
            stateCode = entity.stateCode,
            latitude = entity.latitude,
            longitude = entity.longitude,
            zoomFactor = entity.zoomFactor,
            country = com.parking.management.features.country.models.CountryMapper.toResponse(entity.country),
            createdAt = entity.createdAt!!
        )
}

fun City.merge(cityUpdate: CityUpdate) {
    cityUpdate.name?.let {
        name = it
    }
    cityUpdate.stateCode?.let {
        stateCode = it
    }
    cityUpdate.latitude?.let {
        latitude = it
    }
    cityUpdate.longitude?.let {
        longitude = it
    }
    cityUpdate.zoomFactor?.let {
        zoomFactor = it
    }
}
