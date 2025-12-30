package com.parking.management.features.city

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CityRepository : JpaRepository<City, UUID> {
    fun findByName(name: String): City?
    fun findAllByCountryId(countryId: UUID, pageable: Pageable): Page<City>
}
