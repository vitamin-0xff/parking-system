package com.parking.management.features.parking_spot

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ParkingSpotRepository : JpaRepository<ParkingSpot, UUID> {
    fun findByParkingIdAndLevelAndSpotNumber(parkingId: String, level: String, spotNumber: String): ParkingSpot?
    fun findAllByParkingId(parkingId: String, pageable: Pageable): Page<ParkingSpot>
    fun findAllByParkingIdAndIsOccupied(parkingId: String, isOccupied: Boolean, pageable: Pageable): Page<ParkingSpot>
}