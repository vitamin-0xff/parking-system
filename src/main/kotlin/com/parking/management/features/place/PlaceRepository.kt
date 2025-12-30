package com.parking.management.features.place

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PlaceRepository : JpaRepository<Place, UUID> {
    fun findByName(name: String): Place?
    fun findAllByCityId(cityId: UUID, pageable: Pageable): Page<Place>
}
