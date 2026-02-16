package com.parking.management.features.country

import com.parking.management.features.country.models.CountryFuzzySearchResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface CountryRepository : JpaRepository<Country, UUID>, JpaSpecificationExecutor<Country> {
    fun findByName(name: String): Country?
    fun findByIsoCode(isoCode: String): Country?
    fun existsByIsoCode(isoCode: String): Boolean

    /* Postgres specific query */
    @Query("""
          SELECT 
          c.id as id,
          c.name AS name,
          c.latitude AS latitude,
          c.longitude AS longitude,
          c.zoom_factor AS zoomFactor,
          c.iso_code AS isoCode,
          similarity(c.name, :name) AS sim
          FROM countries c
          WHERE similarity(c.name, :name) > :threshold
          ORDER BY sim DESC
          LIMIT :limit""",
        nativeQuery = true
    )
    fun fuzzySearch(@Param("name")  name: String, @Param("threshold") threshold: Double, @Param("limit") limit: Int): List<CountryFuzzySearchResponse>
}
