package com.parking.management.features.parking_event

import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.features.card.CardRepository

import com.parking.management.features.entry_gate.EntryGateRepository
import com.parking.management.features.parking.ParkingRepository
import com.parking.management.features.parking_event.models.ParkingEventCreate
import com.parking.management.features.parking_event.models.ParkingEventMapper
import com.parking.management.features.parking_event.models.ParkingEventResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class ParkingEventService(
    val repository: ParkingEventRepository,
    val cardRepository: CardRepository,
    val parkingRepository: ParkingRepository,
    val entryGateRepository: EntryGateRepository
) {
    fun create(parkingEventCreate: ParkingEventCreate): ParkingEventResponse {
        val card = cardRepository.findById(parkingEventCreate.cardId).orElseThrow { NotFoundException("${parkingEventCreate.cardId} card not exists") }
        val parking = parkingRepository.findById(parkingEventCreate.parkingId).orElseThrow { NotFoundException("${parkingEventCreate.parkingId} parking not exists") }
        val entryGate = entryGateRepository.findById(parkingEventCreate.entryGateId).orElseThrow { NotFoundException("${parkingEventCreate.entryGateId} entryGate not exists") }
        val parkingEvent = ParkingEventMapper.toEntity(parkingEventCreate, card, parking, entryGate)
        return ParkingEventMapper.toResponse(repository.save(parkingEvent))
    }

    fun createList(parkingEventsCreate: List<ParkingEventCreate>): List<ParkingEventResponse> {
        return parkingEventsCreate.map { create(it) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): ParkingEventResponse {
        return ParkingEventMapper.toResponse(repository.findById(uuid).orElseThrow { NotFoundException("$uuid parkingEvent not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable, deletedIncluded: Boolean = false): Page<ParkingEventResponse> {
        return if (deletedIncluded) repository.findAll(pageable).map { ParkingEventMapper.toResponse(it) } else repository.findAllByDeletedAtIsNull(pageable).map { ParkingEventMapper.toResponse(it) }
    }


    fun delete(parkingEventId: UUID): Message {
        val parkingEvent = repository.findById(parkingEventId).orElseThrow { NotFoundException("$parkingEventId parkingEvent not exists") }
        parkingEvent.deletedAt = LocalDateTime.now()
        repository.save(parkingEvent)
        return Message("Parking Event deleted successfully")
    }
}