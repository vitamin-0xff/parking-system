package com.parking.management.features.city

import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.ed.events.CityEvents
import com.parking.management.features.city.models.CityCreate
import com.parking.management.features.city.models.CityMapper
import com.parking.management.features.city.models.CityResponse
import com.parking.management.features.city.models.CitySpecificationsDto
import com.parking.management.features.city.models.CityUpdate
import com.parking.management.features.city.models.merge
import com.parking.management.features.country.CountryRepository
import com.parking.management.specifications.SpecsFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class CityService(
    val repository: CityRepository,
    val countryRepository: CountryRepository,
    val cityEvents: CityEvents
) {

    fun create(cityCreate: CityCreate): CityResponse {
        val country = countryRepository.findById(cityCreate.countryId).orElseThrow { NotFoundException("Country with id ${cityCreate.countryId} not found") }
        val city = City(
            name = cityCreate.name,
            stateCode = cityCreate.stateCode,
            country = country,
            latitude = cityCreate.latitude,
            longitude = cityCreate.longitude,
            zoomFactor = cityCreate.zoomFactor
        )
        val savedCity = repository.save(city)
        cityEvents.publishCityCreated(savedCity.id.toString())
        return CityMapper.toResponse(savedCity)
    }

    fun createList(citiesCreate: List<CityCreate>): List<CityResponse> {
        val cities = citiesCreate.map {
            val country = countryRepository.findById(it.countryId).orElseThrow { NotFoundException("Country with id ${it.countryId} not found") }
            City(
                name = it.name,
                stateCode = it.stateCode,
                country = country,
                latitude = it.latitude,
                longitude = it.longitude,
                zoomFactor = it.zoomFactor
            )
        }
        val savedCities = repository.saveAll(cities)
        savedCities.forEach { cityEvents.publishCityCreated(it.id.toString()) }
        return savedCities.map { CityMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): CityResponse {
        return CityMapper.toResponse(repository.findById(uuid).orElseThrow { NotFoundException("$uuid City not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable): Page<CityResponse> {
        return repository.findAll(pageable).map { CityMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getAllFilter(pageable: Pageable, filterList: CitySpecificationsDto? = null): Page<CityResponse> {
        if (filterList == null) return repository.findAll(pageable).map { CityMapper.toResponse(it) }
        val filters = mutableListOf<Specification<City>>()
        filterList.names?.let { SpecsFactory.cityByNames(it.listOfStrings)?.let(filters::add) }
        filterList.stateCodes?.let { SpecsFactory.cityByStateCodes(it.listOfStrings)?.let(filters::add) }
        filterList.countryNames?.let { SpecsFactory.cityByCountryNames(it.listOfStrings)?.let(filters::add) }
        filterList.zoomFactor?.let { filters.add(SpecsFactory.cityByZoomFactorRange(it.start, it.end)) }
        filterList.createdAt?.let { filters.add(SpecsFactory.cityByCreatedAtRange(it.start, it.end)) }
        if (filters.isEmpty()) return repository.findAll(pageable).map { CityMapper.toResponse(it) }
        return repository.findAll(Specification.allOf(*filters.toTypedArray()), pageable).map { CityMapper.toResponse(it) }
    }

    fun update(cityId: UUID, cityUpdate: CityUpdate): CityResponse {
        val city = repository.findById(cityId).orElseThrow { NotFoundException("$cityId City not exists") }
        city.merge(cityUpdate)
        cityUpdate.countryId?.let {
            val country = countryRepository.findById(it).orElseThrow { NotFoundException("Country with id $it not found") }
            city.country = country
        }
        val updatedCity = repository.save(city)
        cityEvents.publishCityUpdated(updatedCity.id.toString())
        return CityMapper.toResponse(updatedCity)
    }

    fun delete(cityId: UUID): Message {
        val city = repository.findById(cityId).orElseThrow { NotFoundException("$cityId City not exists") }
        repository.delete(city)
        cityEvents.publishCityDeleted(cityId.toString())
        return Message("City deleted successfully")
    }

    fun findAllByCountryId(countryId: UUID, pageable: Pageable): Page<CityResponse> {
        return repository.findAllByCountryId(countryId, pageable).map { CityMapper.toResponse(it) }
    }
}
