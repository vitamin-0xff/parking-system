package com.parking.management.features.entry_gate.models

import com.parking.management.features.entry_gate.EntryGate
import com.parking.management.features.entry_gate.EntryGateDirection
import com.parking.management.features.parking.models.ParkingResponse
import com.parking.management.specifications.DateRangeFilter
import com.parking.management.specifications.StringListFilter
import jakarta.validation.constraints.*
import java.time.LocalDateTime
import java.util.UUID

/* =========================
   CREATE
   ========================= */

data class EntryGateCreate(

    @field:NotNull(message = "Parking ID is required")
    val parkingId: UUID,

    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotNull(message = "Direction is required")
    val direction: EntryGateDirection,

    @field:NotBlank(message = "Hardware ID is required")
    val hardwareId: String,

    @field:NotNull(message = "Is active is required")
    val isActive: Boolean
)

/* =========================
   UPDATE
   ========================= */

data class EntryGateUpdate(

    val parkingId: UUID? = null,
    val name: String? = null,
    val direction: EntryGateDirection? = null,
    val hardwareId: String? = null,
    val isActive: Boolean? = null
)

/* =========================
   RESPONSE
   ========================= */

data class EntryGateResponse(
    val id: UUID,
    val parking: ParkingResponse,
    val name: String,
    val direction: EntryGateDirection,
    val hardwareId: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime
)

/* =========================
   SPECIFICATIONS DTO
   ========================= */

data class EntryGateSpecificationsDto(
    val names: StringListFilter? = null,
    val directions: List<EntryGateDirection>? = null,
    val hardwareIds: StringListFilter? = null,
    val isActive: Boolean? = null,
    val parkingIds: List<UUID>? = null,
    val createdAt: DateRangeFilter? = null
)

/* =========================
    MAPPER
    ========================= */

object EntryGateMapper {

    fun toResponse(entity: EntryGate): EntryGateResponse =
        EntryGateResponse(
            id = entity.id!!,
            parking = com.parking.management.features.parking.models.ParkingMapper.toResponse(entity.parking),
            name = entity.name,
            direction = entity.direction,
            hardwareId = entity.hardwareId,
            isActive = entity.isActive,
            createdAt = entity.createdAt!!
        )
}

fun EntryGate.merge(entryGateUpdate: EntryGateUpdate) {
    entryGateUpdate.name?.let {
        name = it
    }
    entryGateUpdate.direction?.let {
        direction = it
    }
    entryGateUpdate.hardwareId?.let {
        hardwareId = it
    }
    entryGateUpdate.isActive?.let {
        isActive = it
    }
}
