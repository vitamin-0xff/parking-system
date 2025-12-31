package com.parking.management.features.gate_movement.models

import com.parking.management.features.entry_gate.EntryGate
import com.parking.management.features.entry_gate.models.EntryGateMapper
import com.parking.management.features.entry_gate.models.EntryGateResponse
import com.parking.management.features.gate_movement.GateMovement
import com.parking.management.features.parking.Parking
import com.parking.management.features.parking.models.ParkingMapper
import com.parking.management.features.parking.models.ParkingResponse
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.UUID

/* =========================
   CREATE
   ========================= */

data class GateMovementCreate(
    @field:NotNull(message = "Entry gate ID is required")
    val entryGateId: UUID,
    val previousParkingId: UUID? = null,
    @field:NotNull(message = "New parking ID is required")
    val newParkingId: UUID,
    val reason: String? = null
)

/* =========================
   RESPONSE
   ========================= */

data class GateMovementResponse(
    val id: String, // MongoDB ID is String
    val placedAt: LocalDateTime,
    val entryGate: EntryGateResponse,
    val previousParking: ParkingResponse?,
    val newParking: ParkingResponse,
    val reason: String?,
    val deletedAt: LocalDateTime?
)

/* =========================
   MAPPER
   ========================= */

object GateMovementMapper {
    fun toEntity(
        dto: GateMovementCreate,
        entryGate: EntryGate,
        previousParking: Parking?,
        newParking: Parking
    ): GateMovement =
        GateMovement(
            placedAt = LocalDateTime.now(),
            entryGate = entryGate,
            previousParking = previousParking,
            newParking = newParking,
            reason = dto.reason
        )

    fun toResponse(entity: GateMovement): GateMovementResponse =
        GateMovementResponse(
            id = entity.id!!,
            placedAt = entity.placedAt,
            entryGate = EntryGateMapper.toResponse(entity.entryGate),
            previousParking = entity.previousParking?.let { ParkingMapper.toResponse(it) },
            newParking = ParkingMapper.toResponse(entity.newParking),
            reason = entity.reason,
            deletedAt = entity.deletedAt
        )
}
