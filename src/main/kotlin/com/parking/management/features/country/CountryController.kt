package com.parking.management.features.country

import com.parking.management.comman.models.Message
import com.parking.management.features.country.models.CountryCreate
import com.parking.management.features.country.models.CountryFuzzySearchResponse
import com.parking.management.features.country.models.CountryResponse
import com.parking.management.features.country.models.CountryUpdate
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/api/countries")
class CountryController(
    val countryService: CountryService
) {
    @PostMapping
    fun create(@Valid @RequestBody countryCreate: CountryCreate): CountryResponse {
        return countryService.create(countryCreate)
    }

    @PostMapping("/all")
    fun createList(@Valid @RequestBody countriesCreate: List<CountryCreate>): List<CountryResponse> {
        return countryService.createList(countriesCreate)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): CountryResponse {
        return countryService.getById(id)
    }

    @GetMapping
    fun getAll(
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable): Page<CountryResponse> {
        return countryService.getAll(pageable)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody countryUpdate: CountryUpdate): CountryResponse {
        return countryService.update(id, countryUpdate)
    }

    @GetMapping("/fuzzySearch")
    fun fuzzySearch(
        @RequestParam name: String,
        @RequestParam(defaultValue = "0.3") threshold: Double,
        @RequestParam(defaultValue = "10") limit: Int
    ): List<CountryFuzzySearchResponse> {
        return countryService.fuzzySearch(name, threshold, limit)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): Message {
        return countryService.delete(id)
    }
}
