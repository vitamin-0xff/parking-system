package com.parking.management.features.place.models

import com.parking.management.features.city.models.CityResponse
import com.parking.management.features.place.Place
import jakarta.validation.constraints.*
import java.time.LocalDateTime
import java.util.UUID

/* =========================
   CREATE
   ========================= */

data class PlaceCreate(

    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "Address line is required")
    val addressLine: String,

    @field:NotNull(message = "City ID is required")
    val cityId: UUID,

    @field:NotNull(message = "Latitude is required")
    val latitude: Float,

    @field:NotNull(message = "Longitude is required")
    val longitude: Float
)

/* =========================
   UPDATE
   ========================= */

data class PlaceUpdate(

    val name: String? = null,
    val addressLine: String? = null,
    val cityId: UUID? = null,
    val latitude: Float? = null,
    val longitude: Float? = null
)

/* =========================
   RESPONSE
   ========================= */

data class PlaceResponse(
    val id: UUID,
    val name: String,
    val addressLine: String,
    val city: CityResponse,
    val latitude: Float,
    val longitude: Float,
    val createdAt: LocalDateTime
)

/* =========================
   MAPPER
   ========================= */

object PlaceMapper {

    fun toResponse(entity: Place): PlaceResponse =
        PlaceResponse(
            id = entity.id!!,
            name = entity.name,
            addressLine = entity.addressLine,
            city = com.parking.management.features.city.models.CityMapper.toResponse(entity.city),
            latitude = entity.latitude,
            longitude = entity.longitude,
            createdAt = entity.createdAt!!
        )
}

fun Place.merge(placeUpdate: PlaceUpdate) {
    placeUpdate.name?.let {
        name = it
    }
    placeUpdate.addressLine?.let {
        addressLine = it
    }
    placeUpdate.latitude?.let {
        latitude = it
    }
    placeUpdate.longitude?.let {
        longitude = it
    }
}
