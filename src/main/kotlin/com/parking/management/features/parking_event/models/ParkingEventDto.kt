package com.parking.management.features.parking_event.models

import com.example.parking.client.api.ClientMapper
import com.example.parking.client.api.ClientResponse
import com.parking.management.features.card.Card
import com.parking.management.features.card.models.CardMapper
import com.parking.management.features.card.models.CardResponse
import com.parking.management.features.entry_gate.EntryGate
import com.parking.management.features.entry_gate.models.EntryGateMapper
import com.parking.management.features.entry_gate.models.EntryGateResponse
import com.parking.management.features.parking.Parking
import com.parking.management.features.parking.models.ParkingMapper
import com.parking.management.features.parking.models.ParkingResponse
import com.parking.management.features.parking_event.Direction
import com.parking.management.features.parking_event.ParkingEvent
import jakarta.validation.constraints.*
import java.time.LocalDateTime
import java.util.UUID

/* =========================
   CREATE
   ========================= */

data class ParkingEventCreate(
    @field:NotNull(message = "Card id is required")
    var cardId: UUID,
    @field:NotNull(message = "Parking id is required")
    var parkingId: UUID,
    @field:NotNull(message = "Entry gate id is required")
    var entryGateId: UUID,
    @field:NotNull(message = "Direction id is required")
    var direction: Direction,
    var creditsCharged: Float = 1f
)

/* =========================
   RESPONSE
   ========================= */

data class ParkingEventResponse(
    val id: UUID,
    val client: ClientResponse,
    val card: CardResponse,
    val parking: ParkingResponse,
    val entryGate: EntryGateResponse,
    val direction: Direction,
    val creditsCharged: Float,
    val timestamp: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

/* =========================
   MAPPER
   ========================= */

object ParkingEventMapper {

    fun toEntity(dto: ParkingEventCreate, card: Card, parking: Parking, entryGate: EntryGate): ParkingEvent =
        ParkingEvent(
            card = card,
            parking = parking,
            entryGate = entryGate,
            direction = dto.direction,
            chargedCredits = 1f,
            timestamp = LocalDateTime.now()
        )

    fun toResponse(entity: ParkingEvent): ParkingEventResponse =
        ParkingEventResponse(
            client = ClientMapper.toResponse(entity.card.client),
            id = entity.id!!,
            card = CardMapper.toResponse(entity.card, ClientMapper.toResponse(entity.card.client)),
            parking = ParkingMapper.toResponse(entity.parking),
            entryGate = EntryGateMapper.toResponse(entity.entryGate),
            creditsCharged = entity.chargedCredits,
            direction = entity.direction,
            timestamp = entity.timestamp,
            createdAt = entity.createdAt!!,
            updatedAt = entity.updatedAt!!
        )
}

