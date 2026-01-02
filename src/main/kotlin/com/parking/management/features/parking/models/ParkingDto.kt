package com.parking.management.features.parking.models

import com.parking.management.features.city.models.CityMapper
import com.parking.management.features.city.models.CityResponse
import com.parking.management.features.parking.Parking
import com.parking.management.features.parking.ParkingStatus
import com.parking.management.features.place.models.PlaceResponse
import jakarta.validation.constraints.*
import java.time.LocalDateTime
import java.util.UUID

/* =========================
   CREATE
   ========================= */

data class ParkingCreate(

    @field:NotNull(message = "Place ID is required")
    val cityId: UUID,

    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotNull(message = "Latitude is required")
    val latitude: Float,

    @field:NotNull(message = "Longitude is required")
    val longitude: Float,

    @field:NotNull(message = "Total capacity is required")
    val totalCapacity: Int,

    @field:NotNull(message = "Current occupied is required")
    val currentOccupied: Int,

    @field:NotNull(message = "Status is required")
    val status: ParkingStatus
)

/* =========================
   UPDATE
   ========================= */

data class ParkingUpdate(

    val cityId: UUID? = null,
    val name: String? = null,
    val latitude: Float? = null,
    val longitude: Float? = null,
    val totalCapacity: Int? = null,
    val currentOccupied: Int? = null,
    val status: ParkingStatus? = null
)

/* =========================
   RESPONSE
   ========================= */

data class ParkingResponse(
    val id: UUID,
    val city: CityResponse,
    val name: String,
    val latitude: Float,
    val longitude: Float,
    val totalCapacity: Int,
    val currentOccupied: Int,
    val status: ParkingStatus,
    val createdAt: LocalDateTime
)

/* =========================
   MAPPER
   ========================= */

object ParkingMapper {

    fun toResponse(entity: Parking): ParkingResponse =
        ParkingResponse(
            id = entity.id!!,
            city = CityMapper.toResponse(entity.city),
            name = entity.name,
            latitude = entity.latitude,
            longitude = entity.longitude,
            totalCapacity = entity.totalCapacity,
            currentOccupied = entity.currentOccupied,
            status = entity.status,
            createdAt = entity.createdAt!!
        )
}

fun Parking.merge(parkingUpdate: ParkingUpdate) {
    parkingUpdate.name?.let {
        name = it
    }
    parkingUpdate.latitude?.let {
        latitude = it
    }
    parkingUpdate.longitude?.let {
        longitude = it
    }
    parkingUpdate.totalCapacity?.let {
        totalCapacity = it
    }
    parkingUpdate.currentOccupied?.let {
        currentOccupied = it
    }
    parkingUpdate.status?.let {
        status = it
    }
}
