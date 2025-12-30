package com.parking.management.features.parking_spot

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ParkingSpotRepository : JpaRepository<ParkingSpot, UUID> {
    fun findByParkingIdAndLevelAndSpotNumber(parkingId: UUID, level: String, spotNumber: String): ParkingSpot?
    fun findAllByParkingId(parkingId: UUID, pageable: Pageable): Page<ParkingSpot>
    fun findAllByParkingIdAndIsOccupied(parkingId: UUID, isOccupied: Boolean, pageable: Pageable): Page<ParkingSpot>
}