package com.parking.management.features.gate_movement

import com.parking.management.features.entry_gate.EntryGate
import com.parking.management.features.parking.Parking
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document(collection = "gate_movements")
data class GateMovement(
    @Id
    val id: String? = null,
    val placedAt: LocalDateTime,
    val entryGate: EntryGate,
    val previousParking: Parking?,
    val newParking: Parking,
    val reason: String?,
    var deletedAt: LocalDateTime? = null
)
