package com.parking.management.features.entry_gate

import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.features.entry_gate.models.EntryGateCreate
import com.parking.management.features.entry_gate.models.EntryGateMapper
import com.parking.management.features.entry_gate.models.EntryGateResponse
import com.parking.management.features.entry_gate.models.EntryGateUpdate
import com.parking.management.features.entry_gate.models.merge
import com.parking.management.features.parking.ParkingRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class EntryGateService(
    val repository: EntryGateRepository,
    val parkingRepository: ParkingRepository
) {

    fun create(entryGateCreate: EntryGateCreate): EntryGateResponse {
        val parking = parkingRepository.findById(entryGateCreate.parkingId).orElseThrow { NotFoundException("Parking with id ${entryGateCreate.parkingId} not found") }
        val entryGate = EntryGate(
            parking = parking,
            name = entryGateCreate.name,
            direction = entryGateCreate.direction,
            hardwareId = entryGateCreate.hardwareId,
            isActive = entryGateCreate.isActive
        )
        return EntryGateMapper.toResponse(repository.save(entryGate))
    }

    fun createList(entryGatesCreate: List<EntryGateCreate>): List<EntryGateResponse> {
        val entryGates = entryGatesCreate.map {
            val parking = parkingRepository.findById(it.parkingId).orElseThrow { NotFoundException("Parking with id ${it.parkingId} not found") }
            EntryGate(
                parking = parking,
                name = it.name,
                direction = it.direction,
                hardwareId = it.hardwareId,
                isActive = it.isActive
            )
        }
        return repository.saveAll(entryGates).map { EntryGateMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): EntryGateResponse {
        return EntryGateMapper.toResponse(repository.findById(uuid).orElseThrow { NotFoundException("$uuid EntryGate not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable): Page<EntryGateResponse> {
        return repository.findAll(pageable).map { EntryGateMapper.toResponse(it) }
    }

    fun update(entryGateId: UUID, entryGateUpdate: EntryGateUpdate): EntryGateResponse {
        val entryGate = repository.findById(entryGateId).orElseThrow { NotFoundException("$entryGateId EntryGate not exists") }
        entryGate.merge(entryGateUpdate)
        entryGateUpdate.parkingId?.let {
            val parking = parkingRepository.findById(it).orElseThrow { NotFoundException("Parking with id $it not found") }
            entryGate.parking = parking
        }
        return EntryGateMapper.toResponse(repository.save(entryGate))
    }

    fun delete(entryGateId: UUID): Message {
        val entryGate = repository.findById(entryGateId).orElseThrow { NotFoundException("$entryGateId EntryGate not exists") }
        repository.delete(entryGate)
        return Message("EntryGate deleted successfully")
    }

    fun findAllByParkingId(parkingId: UUID, pageable: Pageable): Page<EntryGateResponse> {
        return repository.findAllByParkingId(parkingId, pageable).map { EntryGateMapper.toResponse(it) }
    }
}
