package com.parking.management.features.parking

import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.features.city.CityRepository
import com.parking.management.features.parking.models.ParkingCreate
import com.parking.management.features.parking.models.ParkingMapper
import com.parking.management.features.parking.models.ParkingResponse
import com.parking.management.features.parking.models.ParkingUpdate
import com.parking.management.features.parking.models.merge
import com.parking.management.features.place.PlaceRepository
import com.parking.management.specifications.Filter
import com.parking.management.specifications.FilterObject
import com.parking.management.specifications.SpecificationsDto
import com.parking.management.specifications.SpecificationsType
import com.parking.management.specifications.SpecsFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class ParkingService(
    val repository: ParkingRepository,
    val cityRepository: CityRepository
) {

    fun create(parkingCreate: ParkingCreate): ParkingResponse {
        val city = cityRepository.findById(parkingCreate.cityId).orElseThrow { NotFoundException("City with id ${parkingCreate.cityId} not found") }
        val parking = Parking(
            city = city,
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
            val city = cityRepository.findById(it.cityId).orElseThrow { NotFoundException("City with id ${it.cityId} not found") }
            Parking(
                city = city,
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
    fun getAll(pageable: Pageable, filterList: List<FilterObject>? = null): Page<ParkingResponse> {
        if(filterList == null) return repository.findAll(pageable).map { ParkingMapper.toResponse(it) }
        val specifications = SpecsFactory.generalFilter<Parking>(filterList)
        return repository.findAll(specifications, pageable).map { ParkingMapper.toResponse(it) }
    }

    fun update(parkingId: UUID, parkingUpdate: ParkingUpdate): ParkingResponse {
        val parking = repository.findById(parkingId).orElseThrow { NotFoundException("$parkingId Parking not exists") }
        parking.merge(parkingUpdate)
        parkingUpdate.cityId?.let {
            val city = cityRepository.findById(it).orElseThrow { NotFoundException("Place with id $it not found") }
            parking.city = city
        }
        return ParkingMapper.toResponse(repository.save(parking))
    }

    fun delete(parkingId: UUID): Message {
        val parking = repository.findById(parkingId).orElseThrow { NotFoundException("$parkingId Parking not exists") }
        repository.delete(parking)
        return Message("Parking deleted successfully")
    }

    fun findAllByPlaceId(cityId: UUID, pageable: Pageable): Page<ParkingResponse> {
        return repository.findAllByCityId(cityId, pageable).map { ParkingMapper.toResponse(it) }
    }
}
