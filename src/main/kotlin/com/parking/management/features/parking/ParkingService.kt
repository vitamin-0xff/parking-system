package com.parking.management.features.parking

import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.features.parking.models.ParkingCreate
import com.parking.management.features.parking.models.ParkingMapper
import com.parking.management.features.parking.models.ParkingResponse
import com.parking.management.features.parking.models.ParkingUpdate
import com.parking.management.features.parking.models.merge
import com.parking.management.features.place.PlaceRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class ParkingService(
    val repository: ParkingRepository,
    val placeRepository: PlaceRepository
) {

    fun create(parkingCreate: ParkingCreate): ParkingResponse {
        val place = placeRepository.findById(parkingCreate.placeId).orElseThrow { NotFoundException("Place with id ${parkingCreate.placeId} not found") }
        val parking = Parking(
            place = place,
            name = parkingCreate.name,
            latitude = parkingCreate.latitude,
            longitude = parkingCreate.longitude,
            totalCapacity = parkingCreate.totalCapacity,
            currentOccupied = parkingCreate.currentOccupied,
            status = parkingCreate.status
        )
        return ParkingMapper.toResponse(repository.save(parking))
    }

    fun createList(parkingsCreate: List<ParkingCreate>): List<ParkingResponse> {
        val parkings = parkingsCreate.map {
            val place = placeRepository.findById(it.placeId).orElseThrow { NotFoundException("Place with id ${it.placeId} not found") }
            Parking(
                place = place,
                name = it.name,
                latitude = it.latitude,
                longitude = it.longitude,
                totalCapacity = it.totalCapacity,
                currentOccupied = it.currentOccupied,
                status = it.status
            )
        }
        return repository.saveAll(parkings).map { ParkingMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): ParkingResponse {
        return ParkingMapper.toResponse(repository.findById(uuid).orElseThrow { NotFoundException("$uuid Parking not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable): Page<ParkingResponse> {
        return repository.findAll(pageable).map { ParkingMapper.toResponse(it) }
    }

    fun update(parkingId: UUID, parkingUpdate: ParkingUpdate): ParkingResponse {
        val parking = repository.findById(parkingId).orElseThrow { NotFoundException("$parkingId Parking not exists") }
        parking.merge(parkingUpdate)
        parkingUpdate.placeId?.let {
            val place = placeRepository.findById(it).orElseThrow { NotFoundException("Place with id $it not found") }
            parking.place = place
        }
        return ParkingMapper.toResponse(repository.save(parking))
    }

    fun delete(parkingId: UUID): Message {
        val parking = repository.findById(parkingId).orElseThrow { NotFoundException("$parkingId Parking not exists") }
        repository.delete(parking)
        return Message("Parking deleted successfully")
    }

    fun findAllByPlaceId(placeId: UUID, pageable: Pageable): Page<ParkingResponse> {
        return repository.findAllByPlaceId(placeId, pageable).map { ParkingMapper.toResponse(it) }
    }
}
