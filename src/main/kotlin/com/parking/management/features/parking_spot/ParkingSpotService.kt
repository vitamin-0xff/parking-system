package com.parking.management.features.parking_spot

import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.features.parking.ParkingRepository
import com.parking.management.features.parking_spot.models.ParkingSpotCreate
import com.parking.management.features.parking_spot.models.ParkingSpotMapper
import com.parking.management.features.parking_spot.models.ParkingSpotResponse
import com.parking.management.features.parking_spot.models.ParkingSpotUpdate
import com.parking.management.features.parking_spot.models.merge
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class ParkingSpotService(
    val repository: ParkingSpotRepository,
    val parkingRepository: ParkingRepository
) {

    fun create(parkingSpotCreate: ParkingSpotCreate): ParkingSpotResponse {
        val parking = parkingRepository.findById(parkingSpotCreate.parkingId).orElseThrow { NotFoundException("Parking with id ${parkingSpotCreate.parkingId} not found") }
        val parkingSpot = ParkingSpotMapper.toEntity(parkingSpotCreate, parking)
        return ParkingSpotMapper.toResponse(repository.save(parkingSpot))
    }

    fun createList(parkingSpotsCreate: List<ParkingSpotCreate>): List<ParkingSpotResponse> {
        val parkingSpots = parkingSpotsCreate.map { 
            val parking = parkingRepository.findById(it.parkingId).orElseThrow { NotFoundException("Parking with id ${it.parkingId} not found") }
            ParkingSpotMapper.toEntity(it, parking) 
        }
        return repository.saveAll(parkingSpots).map { ParkingSpotMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): ParkingSpotResponse {
        return ParkingSpotMapper.toResponse(repository.findById(uuid).orElseThrow { NotFoundException("$uuid ParkingSpot not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable): Page<ParkingSpotResponse> {
        return repository.findAll(pageable).map { ParkingSpotMapper.toResponse(it) }
    }

    fun update(parkingSpotId: UUID, parkingSpotUpdate: ParkingSpotUpdate): ParkingSpotResponse {
        val parkingSpot = repository.findById(parkingSpotId).orElseThrow { NotFoundException("$parkingSpotId ParkingSpot not exists") }
        parkingSpot.merge(parkingSpotUpdate)
        return ParkingSpotMapper.toResponse(repository.save(parkingSpot))
    }

    fun delete(parkingSpotId: UUID): Message {
        val parkingSpot = repository.findById(parkingSpotId).orElseThrow { NotFoundException("$parkingSpotId ParkingSpot not exists") }
        repository.delete(parkingSpot)
        return Message("ParkingSpot deleted successfully")
    }

    fun findByParkingId(parkingId: UUID, pageable: Pageable): Page<ParkingSpotResponse> {
        return repository.findAllByParkingId(parkingId, pageable).map { ParkingSpotMapper.toResponse(it) }
    }

    fun findAvailableParkingSpots(parkingId: UUID, pageable: Pageable): Page<ParkingSpotResponse> {
        return repository.findAllByParkingIdAndIsOccupied(parkingId, false, pageable).map { ParkingSpotMapper.toResponse(it) }
    }

    fun occupyParkingSpot(parkingSpotId: UUID): ParkingSpotResponse {
        val parkingSpot = repository.findById(parkingSpotId).orElseThrow { NotFoundException("$parkingSpotId ParkingSpot not exists") }
        parkingSpot.isOccupied = true
        return ParkingSpotMapper.toResponse(repository.save(parkingSpot))
    }

    fun freeParkingSpot(parkingSpotId: UUID): ParkingSpotResponse {
        val parkingSpot = repository.findById(parkingSpotId).orElseThrow { NotFoundException("$parkingSpotId ParkingSpot not exists") }
        parkingSpot.isOccupied = false
        return ParkingSpotMapper.toResponse(repository.save(parkingSpot))
    }

}