package com.parking.management.features.parking

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ParkingRepository : JpaRepository<Parking, UUID> {
    fun findByName(name: String): Parking?
    fun findAllByCityId(cityId: UUID, pageable: Pageable): Page<Parking>
}
