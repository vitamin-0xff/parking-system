package com.parking.management.features.city

import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.features.city.models.CityCreate
import com.parking.management.features.city.models.CityMapper
import com.parking.management.features.city.models.CityResponse
import com.parking.management.features.city.models.CityUpdate
import com.parking.management.features.city.models.merge
import com.parking.management.features.country.CountryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class CityService(
    val repository: CityRepository,
    val countryRepository: CountryRepository
) {

    fun create(cityCreate: CityCreate): CityResponse {
        val country = countryRepository.findById(cityCreate.countryId).orElseThrow { NotFoundException("Country with id ${cityCreate.countryId} not found") }
        val city = City(
            name = cityCreate.name,
            postalCode = cityCreate.postalCode,
            stateCode = cityCreate.stateCode,
            country = country,
            latitude = cityCreate.latitude,
            longitude = cityCreate.longitude,
            zoomFactor = cityCreate.zoomFactor
        )
        return CityMapper.toResponse(repository.save(city))
    }

    fun createList(citiesCreate: List<CityCreate>): List<CityResponse> {
        val cities = citiesCreate.map {
            val country = countryRepository.findById(it.countryId).orElseThrow { NotFoundException("Country with id ${it.countryId} not found") }
            City(
                name = it.name,
                postalCode = it.postalCode,
                stateCode = it.stateCode,
                country = country,
                latitude = it.latitude,
                longitude = it.longitude,
                zoomFactor = it.zoomFactor
            )
        }
        return repository.saveAll(cities).map { CityMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): CityResponse {
        return CityMapper.toResponse(repository.findById(uuid).orElseThrow { NotFoundException("$uuid City not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable): Page<CityResponse> {
        return repository.findAll(pageable).map { CityMapper.toResponse(it) }
    }

    fun update(cityId: UUID, cityUpdate: CityUpdate): CityResponse {
        val city = repository.findById(cityId).orElseThrow { NotFoundException("$cityId City not exists") }
        city.merge(cityUpdate)
        cityUpdate.countryId?.let {
            val country = countryRepository.findById(it).orElseThrow { NotFoundException("Country with id $it not found") }
            city.country = country
        }
        return CityMapper.toResponse(repository.save(city))
    }

    fun delete(cityId: UUID): Message {
        val city = repository.findById(cityId).orElseThrow { NotFoundException("$cityId City not exists") }
        repository.delete(city)
        return Message("City deleted successfully")
    }

    fun findAllByCountryId(countryId: UUID, pageable: Pageable): Page<CityResponse> {
        return repository.findAllByCountryId(countryId, pageable).map { CityMapper.toResponse(it) }
    }
}
