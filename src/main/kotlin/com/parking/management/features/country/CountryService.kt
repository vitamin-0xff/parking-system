package com.parking.management.features.country

import com.parking.management.comman.models.ConflictException
import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.features.country.models.CountryCreate
import com.parking.management.features.country.models.CountryFuzzySearchResponse
import com.parking.management.features.country.models.CountryMapper
import com.parking.management.features.country.models.CountryResponse
import com.parking.management.features.country.models.CountryUpdate
import com.parking.management.features.country.models.merge
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class CountryService(
    val repository: CountryRepository,
) {

    fun create(countryCreate: CountryCreate): CountryResponse {
        if (repository.existsByIsoCode(countryCreate.isoCode)) {
            throw ConflictException("Country with code ${countryCreate.isoCode} already exists")
        }
        val country = CountryMapper.toEntity(countryCreate)
        return CountryMapper.toResponse(repository.save(country))
    }

    fun createList(countriesCreate: List<CountryCreate>): List<CountryResponse> {
        val countries = countriesCreate.map { CountryMapper.toEntity(it) }
        return repository.saveAll(countries).map { CountryMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): CountryResponse {
        return CountryMapper.toResponse(repository.findById(uuid).orElseThrow { NotFoundException("$uuid Country not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable): Page<CountryResponse> {
        return repository.findAll(pageable).map { CountryMapper.toResponse(it) }
    }

    fun update(countryId: UUID, countryUpdate: CountryUpdate): CountryResponse {
        val country = repository.findById(countryId).orElseThrow { NotFoundException("$countryId Country not exists") }
        country.merge(countryUpdate)
        return CountryMapper.toResponse(repository.save(country))
    }

    fun delete(countryId: UUID): Message {
        val country = repository.findById(countryId).orElseThrow { NotFoundException("$countryId Country not exists") }
        repository.delete(country)
        return Message("Country deleted successfully")
    }

    fun fuzzySearch(name: String, threshold: Double, limit: Int): List<CountryFuzzySearchResponse> {
        return repository.fuzzySearch(name, threshold, limit)
    }
}
