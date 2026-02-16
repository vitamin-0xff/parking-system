package com.parking.management.features.country.models

import com.parking.management.features.country.Country
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

data class CountryCreate(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "ISO code is required")
    val isoCode: String,

    val latitude: Double,

    val longitude: Double,

    @field:Range(max = 25, min = 0, message = "Zoom factor must be between 1 and 25")
    val zoomFactor: Int
)

/* =========================
   UPDATE
   ========================= */

data class CountryUpdate(
    val name: String? = null,
    val isoCode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val zoomFactor: Int? = null
)

/* =========================
   RESPONSE
   ========================= */

data class CountryResponse(
    val id: UUID,
    val name: String,
    val isoCode: String,
    val latitude: Double,
    val longitude: Double,
    val zoomFactor: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

/* =========================
   SPECIFICATIONS DTO
   ========================= */

data class CountrySpecificationsDto(
    val names: StringListFilter? = null,
    val isoCodes: StringListFilter? = null,
    val zoomFactor: RangeFilter? = null,
    val createdAt: DateRangeFilter? = null
)

/* =========================
    MAPPER
    ========================= */

object CountryMapper {

    fun toEntity(dto: CountryCreate): Country =
        Country(
            name = dto.name,
            isoCode = dto.isoCode,
            latitude = dto.latitude,
            longitude = dto.longitude,
            zoomFactor = dto.zoomFactor
        )

    fun toResponse(entity: Country): CountryResponse =
        CountryResponse(
            id = entity.id!!,
            name = entity.name,
            isoCode = entity.isoCode,
            latitude = entity.latitude,
            longitude = entity.longitude,
            zoomFactor = entity.zoomFactor,
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
    countryUpdate.longitude?.let {
        longitude = it
    }
    countryUpdate.latitude?.let {
        latitude = it
    }
    countryUpdate.zoomFactor?.let {
        zoomFactor = it
    }
}
