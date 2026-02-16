package com.parking.management.features.entry_gate

import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.ed.events.EntryGateEvents
import com.parking.management.features.entry_gate.models.EntryGateCreate
import com.parking.management.features.entry_gate.models.EntryGateMapper
import com.parking.management.features.entry_gate.models.EntryGateResponse
import com.parking.management.features.entry_gate.models.EntryGateSpecificationsDto
import com.parking.management.features.entry_gate.models.EntryGateUpdate
import com.parking.management.features.entry_gate.models.merge
import com.parking.management.features.parking.ParkingRepository
import com.parking.management.specifications.SpecsFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class EntryGateService(
    val repository: EntryGateRepository,
    val parkingRepository: ParkingRepository,
    val entryGateEvents: EntryGateEvents
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
        val savedEntryGate = repository.save(entryGate)
        entryGateEvents.publishEntryGateCreated(savedEntryGate.id.toString())
        return EntryGateMapper.toResponse(savedEntryGate)
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
        val savedEntryGates = repository.saveAll(entryGates)
        savedEntryGates.forEach { entryGateEvents.publishEntryGateCreated(it.id.toString()) }
        return savedEntryGates.map { EntryGateMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): EntryGateResponse {
        return EntryGateMapper.toResponse(repository.findById(uuid).orElseThrow { NotFoundException("$uuid EntryGate not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable): Page<EntryGateResponse> {
        return repository.findAll(pageable).map { EntryGateMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getAllFilter(pageable: Pageable, filterList: EntryGateSpecificationsDto? = null): Page<EntryGateResponse> {
        if (filterList == null) return repository.findAll(pageable).map { EntryGateMapper.toResponse(it) }
        val filters = mutableListOf<Specification<EntryGate>>()
        filterList.names?.let { SpecsFactory.entryGateByNames(it.listOfStrings)?.let(filters::add) }
        filterList.directions?.let { SpecsFactory.entryGateByDirections(it)?.let(filters::add) }
        filterList.hardwareIds?.let { SpecsFactory.entryGateByHardwareIds(it.listOfStrings)?.let(filters::add) }
        filterList.isActive?.let { filters.add(SpecsFactory.entryGateByIsActive(it)) }
        filterList.parkingIds?.let { SpecsFactory.entryGateByParkingIds(it)?.let(filters::add) }
        filterList.createdAt?.let { filters.add(SpecsFactory.entryGateByCreatedAtRange(it.start, it.end)) }
        if (filters.isEmpty()) return repository.findAll(pageable).map { EntryGateMapper.toResponse(it) }
        return repository.findAll(Specification.allOf(*filters.toTypedArray()), pageable).map { EntryGateMapper.toResponse(it) }
    }

    fun update(entryGateId: UUID, entryGateUpdate: EntryGateUpdate): EntryGateResponse {
        val entryGate = repository.findById(entryGateId).orElseThrow { NotFoundException("$entryGateId EntryGate not exists") }
        entryGate.merge(entryGateUpdate)
        entryGateUpdate.parkingId?.let {
            val parking = parkingRepository.findById(it).orElseThrow { NotFoundException("Parking with id $it not found") }
            entryGate.parking = parking
        }
        val updatedEntryGate = repository.save(entryGate)
        entryGateEvents.publishEntryGateUpdated(updatedEntryGate.id.toString())
        return EntryGateMapper.toResponse(updatedEntryGate)
    }

    fun delete(entryGateId: UUID): Message {
        val entryGate = repository.findById(entryGateId).orElseThrow { NotFoundException("$entryGateId EntryGate not exists") }
        repository.delete(entryGate)
        entryGateEvents.publishEntryGateDeleted(entryGateId.toString())
        return Message("EntryGate deleted successfully")
    }

    fun findAllByParkingId(parkingId: UUID, pageable: Pageable): Page<EntryGateResponse> {
        return repository.findAllByParkingId(parkingId, pageable).map { EntryGateMapper.toResponse(it) }
    }
}
