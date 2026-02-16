package com.parking.management.features.country

import com.parking.management.comman.models.ConflictException
import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.ed.events.CountryEvents
import com.parking.management.features.country.models.CountryCreate
import com.parking.management.features.country.models.CountryFuzzySearchResponse
import com.parking.management.features.country.models.CountryMapper
import com.parking.management.features.country.models.CountryResponse
import com.parking.management.features.country.models.CountrySpecificationsDto
import com.parking.management.features.country.models.CountryUpdate
import com.parking.management.features.country.models.merge
import com.parking.management.specifications.SpecsFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class CountryService(
    val repository: CountryRepository,
    val countryEvents: CountryEvents
) {

    fun create(countryCreate: CountryCreate): CountryResponse {
        if (repository.existsByIsoCode(countryCreate.isoCode)) {
            throw ConflictException("Country with code ${countryCreate.isoCode} already exists")
        }
        val country = CountryMapper.toEntity(countryCreate)
        val savedCountry = repository.save(country)
        countryEvents.publishCountryCreated(savedCountry.id.toString())
        return CountryMapper.toResponse(savedCountry)
    }

    fun createList(countriesCreate: List<CountryCreate>): List<CountryResponse> {
        val countries = countriesCreate.map { CountryMapper.toEntity(it) }
        val savedCountries = repository.saveAll(countries)
        savedCountries.forEach { countryEvents.publishCountryCreated(it.id.toString()) }
        return savedCountries.map { CountryMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): CountryResponse {
        return CountryMapper.toResponse(repository.findById(uuid).orElseThrow { NotFoundException("$uuid Country not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable): Page<CountryResponse> {
        return repository.findAll(pageable).map { CountryMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getAllFilter(pageable: Pageable, filterList: CountrySpecificationsDto? = null): Page<CountryResponse> {
        if (filterList == null) return repository.findAll(pageable).map { CountryMapper.toResponse(it) }
        val filters = mutableListOf<Specification<Country>>()
        filterList.names?.let { SpecsFactory.countryByNames(it.listOfStrings)?.let(filters::add) }
        filterList.isoCodes?.let { SpecsFactory.countryByIsoCodes(it.listOfStrings)?.let(filters::add) }
        filterList.zoomFactor?.let { filters.add(SpecsFactory.countryByZoomFactorRange(it.start, it.end)) }
        filterList.createdAt?.let { filters.add(SpecsFactory.countryByCreatedAtRange(it.start, it.end)) }
        if (filters.isEmpty()) return repository.findAll(pageable).map { CountryMapper.toResponse(it) }
        return repository.findAll(Specification.allOf(*filters.toTypedArray()), pageable).map { CountryMapper.toResponse(it) }
    }

    fun update(countryId: UUID, countryUpdate: CountryUpdate): CountryResponse {
        val country = repository.findById(countryId).orElseThrow { NotFoundException("$countryId Country not exists") }
        country.merge(countryUpdate)
        val updatedCountry = repository.save(country)
        countryEvents.publishCountryUpdated(updatedCountry.id.toString())
        return CountryMapper.toResponse(updatedCountry)
    }

    fun delete(countryId: UUID): Message {
        val country = repository.findById(countryId).orElseThrow { NotFoundException("$countryId Country not exists") }
        repository.delete(country)
        countryEvents.publishCountryDeleted(countryId.toString())
        return Message("Country deleted successfully")
    }

    fun fuzzySearch(name: String, threshold: Double, limit: Int): List<CountryFuzzySearchResponse> {
        return repository.fuzzySearch(name, threshold, limit)
    }
}
