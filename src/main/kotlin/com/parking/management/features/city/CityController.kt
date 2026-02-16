package com.parking.management.features.city

import com.parking.management.comman.models.Message
import com.parking.management.features.city.models.CityCreate
import com.parking.management.features.city.models.CityResponse
import com.parking.management.features.city.models.CitySpecificationsDto
import com.parking.management.features.city.models.CityUpdate
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
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/api/cities")
class CityController(
    val cityService: CityService
) {
    @PostMapping
    fun create(@Valid @RequestBody cityCreate: CityCreate): CityResponse {
        return cityService.create(cityCreate)
    }

    @PostMapping("/all")
    fun createList(@Valid @RequestBody citiesCreate: List<CityCreate>): List<CityResponse> {
        return cityService.createList(citiesCreate)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): CityResponse {
        return cityService.getById(id)
    }

    @GetMapping
    fun getAll(
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable): Page<CityResponse> {
        return cityService.getAll(pageable)
    }

    @PostMapping("/instances/all")
    fun getAllFilter(
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable,
        @RequestBody filter: CitySpecificationsDto? = null
    ): Page<CityResponse> {
        return cityService.getAllFilter(pageable, filter)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody cityUpdate: CityUpdate): CityResponse {
        return cityService.update(id, cityUpdate)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): Message {
        return cityService.delete(id)
    }

    @GetMapping("/country/{countryId}")
    fun getByCountryId(
        @PathVariable countryId: UUID,
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable): Page<CityResponse> {
        return cityService.findAllByCountryId(countryId, pageable)
    }
}
