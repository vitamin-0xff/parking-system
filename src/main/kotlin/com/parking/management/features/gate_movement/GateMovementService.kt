package com.parking.management.features.gate_movement

import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.features.entry_gate.EntryGateRepository
import com.parking.management.features.gate_movement.models.GateMovementCreate
import com.parking.management.features.gate_movement.models.GateMovementMapper
import com.parking.management.features.gate_movement.models.GateMovementResponse
import com.parking.management.features.parking.ParkingRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class GateMovementService(
    val repository: GateMovementRepository,
    val entryGateRepository: EntryGateRepository,
    val parkingRepository: ParkingRepository
) {
    fun create(gateMovementCreate: GateMovementCreate): GateMovementResponse {
        val entryGate = entryGateRepository.findById(gateMovementCreate.entryGateId).orElseThrow { NotFoundException("${gateMovementCreate.entryGateId} entry gate not exists") }

        val previousParking = gateMovementCreate.previousParkingId?.let {
            parkingRepository.findById(it).orElseThrow { NotFoundException("$it previous parking not exists") }
        }

        val newParking = parkingRepository.findById(gateMovementCreate.newParkingId).orElseThrow { NotFoundException("${gateMovementCreate.newParkingId} new parking not exists") }

        val gateMovement = GateMovementMapper.toEntity(gateMovementCreate, entryGate, previousParking, newParking)
        return GateMovementMapper.toResponse(repository.save(gateMovement))
    }

    fun createList(gateMovementCreate: List<GateMovementCreate>): List<GateMovementResponse> {
        val gateMovements = gateMovementCreate.map { create(it) }
        return gateMovements
    }

    @Transactional(readOnly = true)
    fun getById(id: String): GateMovementResponse {
        return GateMovementMapper.toResponse(repository.findById(id).orElseThrow { NotFoundException("$id gate movement not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable, deletedIncluded: Boolean = false): Page<GateMovementResponse> {
        return if (deletedIncluded) repository.findAll(pageable).map { GateMovementMapper.toResponse(it) } else repository.findAllByDeletedAtIsNull(pageable).map { GateMovementMapper.toResponse(it) }
    }

    fun delete(id: String): Message {
        val gateMovement = repository.findById(id).orElseThrow { NotFoundException("$id gate movement not exists") }
        gateMovement.deletedAt = LocalDateTime.now()
        repository.save(gateMovement)
        return Message("Gate Movement deleted successfully")
    }
}
