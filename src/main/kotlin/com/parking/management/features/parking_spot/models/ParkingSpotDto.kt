package com.parking.management.features.parking_spot.models

import com.parking.management.features.parking_spot.ParkingSpot
import com.parking.management.features.parking_spot.ParkingSpotType
import jakarta.validation.constraints.*
import java.time.LocalDateTime
import java.util.UUID

/* =========================
   CREATE
   ========================= */

data class ParkingSpotCreate(

    @field:NotBlank(message = "Parking ID is required")
    val parkingId: String,

    @field:NotBlank(message = "Level is required")
    val level: String,

    @field:NotBlank(message = "Spot number is required")
    val spotNumber: String,

    @field:NotNull(message = "Spot type is required")
    val type: ParkingSpotType
)

/* =========================
   UPDATE
   ========================= */

data class ParkingSpotUpdate(

    val level: String? = null,
    val spotNumber: String? = null,
    val type: ParkingSpotType? = null,
    val isOccupied: Boolean? = null
)

/* =========================
   RESPONSE
   ========================= */

data class ParkingSpotResponse(
    val id: UUID,
    val parkingId: String,
    val level: String,
    val spotNumber: String,
    val type: ParkingSpotType,
    val isOccupied: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

/* =========================
   MAPPER
   ========================= */

object ParkingSpotMapper {

    fun toEntity(dto: ParkingSpotCreate): ParkingSpot =
        ParkingSpot(
            parkingId = dto.parkingId,
            level = dto.level,
            spotNumber = dto.spotNumber,
            type = dto.type
        )

    fun toResponse(entity: ParkingSpot): ParkingSpotResponse =
        ParkingSpotResponse(
            id = entity.id!!,
            parkingId = entity.parkingId,
            level = entity.level,
            spotNumber = entity.spotNumber,
            type = entity.type,
            isOccupied = entity.isOccupied,
            createdAt = entity.createdAt!!,
            updatedAt = entity.updatedAt!!
        )
}

fun ParkingSpot.merge(parkingSpotUpdate: ParkingSpotUpdate) {
    parkingSpotUpdate.level?.let {
        level = it
    }
    parkingSpotUpdate.spotNumber?.let {
        spotNumber = it
    }
    parkingSpotUpdate.type?.let {
        type = it
    }
    parkingSpotUpdate.isOccupied?.let {
        isOccupied = it
    }
}
