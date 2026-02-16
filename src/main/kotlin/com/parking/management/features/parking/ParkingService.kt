package com.parking.management.features.parking

import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.ed.events.ParkingEvents
import com.parking.management.features.city.CityRepository
import com.parking.management.features.filter.FilterRepository
import com.parking.management.features.filter.FilterableService
import com.parking.management.features.filter.parking_filter.FilterRangeUniquesResolverService
import com.parking.management.features.parking.models.ParkingCreate
import com.parking.management.features.parking.models.ParkingMapper
import com.parking.management.features.parking.models.ParkingResponse
import com.parking.management.features.parking.models.ParkingSpecificationsDto
import com.parking.management.features.parking.models.ParkingUpdate
import com.parking.management.features.parking.models.merge
import com.parking.management.specifications.FilterObject
import com.parking.management.specifications.SpecsFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class ParkingService(
    val repository: ParkingRepository,
    val cityRepository: CityRepository,
    val parkingEvents: ParkingEvents,
    val parkingFilterService: FilterRangeUniquesResolverService
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
        val savedParking = repository.save(parking)
        parkingEvents.publishParkingCreated(savedParking.id.toString())
        return ParkingMapper.toResponse(savedParking)
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
        val savedParkings = repository.saveAll(parkings)
        savedParkings.forEach { parkingEvents.publishParkingCreated(it.id.toString()) }
        return savedParkings.map { ParkingMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): ParkingResponse {
        return ParkingMapper.toResponse(repository.findById(uuid).orElseThrow { NotFoundException("$uuid Parking not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable, filterList: List<FilterObject>? = null): Page<ParkingResponse> {
        if (filterList.isNullOrEmpty()) return repository.findAll(pageable).map { ParkingMapper.toResponse(it) }
        val specifications = SpecsFactory.generalFilterWithPathResolver<Parking>(filterList)
        return repository.findAll(specifications, pageable).map { ParkingMapper.toResponse(it) }
    }

    /**
     * {
     * 	"possbileFilters": {
     * 		"currentOccupied": "intRange",
     * 		"totalCapacity": "intRange",
     * 		"city.name": "StringList",
     * 		"country.name": "StringList",
     * 	}
     * }
     */
    @Transactional(readOnly = true)
    fun getAllFilter(pageable: Pageable, filterList: ParkingSpecificationsDto? = null): Page<ParkingResponse> {
        val filters = mutableListOf<Specification<Parking>>()
        if(filterList == null) return repository.findAll(pageable).map { ParkingMapper.toResponse(it) }
        filterList.currentOccupied?.let { filters.add(SpecsFactory.parkingByCurrentOccupiedRange(it.start, it.end)) }
        filterList.totalCapacity?.let { filters.add(SpecsFactory.parkingByTotalCapacityRange(it.start, it.end)) }
        filterList.cityName?.let { SpecsFactory.parkingByCityNames(it.listOfStrings)?.let(filters::add) }
        filterList.countryName?.let { SpecsFactory.parkingByCountryNames(it.listOfStrings)?.let(filters::add) }
        filterList.createdAt?.let { filters.add(SpecsFactory.parkingByCreatedAtRange(it.start, it.end)) }
        if (filters.isEmpty()) return repository.findAll(pageable).map { ParkingMapper.toResponse(it) }
        return repository.findAll(Specification.allOf(*filters.toTypedArray()), pageable).map { ParkingMapper.toResponse(it) }
    }


    fun update(parkingId: UUID, parkingUpdate: ParkingUpdate): ParkingResponse {
        val parking = repository.findById(parkingId).orElseThrow { NotFoundException("$parkingId Parking not exists") }
        parking.merge(parkingUpdate)
        parkingUpdate.cityId?.let {
            val city = cityRepository.findById(it).orElseThrow { NotFoundException("Place with id $it not found") }
            parking.city = city
        }
        val updatedParking = repository.save(parking)
        parkingEvents.publishParkingUpdated(updatedParking.id.toString())
        return ParkingMapper.toResponse(updatedParking)
    }

    fun delete(parkingId: UUID): Message {
        val parking = repository.findById(parkingId).orElseThrow { NotFoundException("$parkingId Parking not exists") }
        repository.delete(parking)
        parkingEvents.publishParkingDeleted(parkingId.toString())
        return Message("Parking deleted successfully")
    }

    fun findAllByPlaceId(cityId: UUID, pageable: Pageable): Page<ParkingResponse> {
        return repository.findAllByCityId(cityId, pageable).map { ParkingMapper.toResponse(it) }
    }

    fun getParkingFilters() = parkingFilterService.getConcreteFilters("parkings", Parking::class.java)
}
