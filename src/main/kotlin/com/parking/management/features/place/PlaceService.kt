package com.parking.management.features.place

import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.features.city.CityRepository
import com.parking.management.features.place.models.PlaceCreate
import com.parking.management.features.place.models.PlaceMapper
import com.parking.management.features.place.models.PlaceResponse
import com.parking.management.features.place.models.PlaceUpdate
import com.parking.management.features.place.models.merge
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class PlaceService(
    val repository: PlaceRepository,
    val cityRepository: CityRepository
) {

    fun create(placeCreate: PlaceCreate): PlaceResponse {
        val city = cityRepository.findById(placeCreate.cityId).orElseThrow { NotFoundException("City with id ${placeCreate.cityId} not found") }
        val place = Place(
            name = placeCreate.name,
            addressLine = placeCreate.addressLine,
            city = city,
            latitude = placeCreate.latitude,
            longitude = placeCreate.longitude
        )
        return PlaceMapper.toResponse(repository.save(place))
    }

    fun createList(placesCreate: List<PlaceCreate>): List<PlaceResponse> {
        val places = placesCreate.map {
            val city = cityRepository.findById(it.cityId).orElseThrow { NotFoundException("City with id ${it.cityId} not found") }
            Place(
                name = it.name,
                addressLine = it.addressLine,
                city = city,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }
        return repository.saveAll(places).map { PlaceMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): PlaceResponse {
        return PlaceMapper.toResponse(repository.findById(uuid).orElseThrow { NotFoundException("$uuid Place not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable): Page<PlaceResponse> {
        return repository.findAll(pageable).map { PlaceMapper.toResponse(it) }
    }

    fun update(placeId: UUID, placeUpdate: PlaceUpdate): PlaceResponse {
        val place = repository.findById(placeId).orElseThrow { NotFoundException("$placeId Place not exists") }
        place.merge(placeUpdate)
        placeUpdate.cityId?.let {
            val city = cityRepository.findById(it).orElseThrow { NotFoundException("City with id $it not found") }
            place.city = city
        }
        return PlaceMapper.toResponse(repository.save(place))
    }

    fun delete(placeId: UUID): Message {
        val place = repository.findById(placeId).orElseThrow { NotFoundException("$placeId Place not exists") }
        repository.delete(place)
        return Message("Place deleted successfully")
    }

    fun findAllByCityId(cityId: UUID, pageable: Pageable): Page<PlaceResponse> {
        return repository.findAllByCityId(cityId, pageable).map { PlaceMapper.toResponse(it) }
    }
}
