package com.parking.management.features.country

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CountryRepository : JpaRepository<Country, UUID> {
    fun findByName(name: String): Country?
    fun findByIsoCode(isoCode: String): Country?
}
