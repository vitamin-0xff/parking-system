package com.parking.management.features.parking_spot

import com.parking.management.comman.models.Message
import com.parking.management.features.parking_spot.models.ParkingSpotCreate
import com.parking.management.features.parking_spot.models.ParkingSpotResponse
import com.parking.management.features.parking_spot.models.ParkingSpotUpdate
import jakarta.validation.Valid
import jakarta.websocket.server.PathParam
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
@RequestMapping("/v1/api/parking-spots")
class ParkingSpotController(
    val parkingSpotService: ParkingSpotService
) {
    @PostMapping
    fun create(@Valid @RequestBody parkingSpotCreate: ParkingSpotCreate): ParkingSpotResponse {
        return parkingSpotService.create(parkingSpotCreate)
    }

    @PostMapping("/all")
    fun createList(@Valid @RequestBody parkingSpotsCreate: List<ParkingSpotCreate>): List<ParkingSpotResponse> {
        return parkingSpotService.createList(parkingSpotsCreate)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ParkingSpotResponse {
        return parkingSpotService.getById(id)
    }

    @GetMapping
    fun getAll(
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable): Page<ParkingSpotResponse> {
        return parkingSpotService.getAll(pageable)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody parkingSpotUpdate: ParkingSpotUpdate): ParkingSpotResponse {
        return parkingSpotService.update(id, parkingSpotUpdate)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): Message {
        return parkingSpotService.delete(id)
    }

    @GetMapping("/parking/{parkingId}")
    fun getByParkingId(
        @PathVariable parkingId: String,
        @PageableDefault(size = 20, page = 0) pageable: Pageable
    ): Page<ParkingSpotResponse> {
        return parkingSpotService.findByParkingId(parkingId, pageable)
    }

    @GetMapping("/parking/{parkingId}/available")
    fun getAvailableParkingSpots(
        @PathVariable parkingId: String,
        @PageableDefault(size = 20, page = 0) pageable: Pageable
    ): Page<ParkingSpotResponse> {
        return parkingSpotService.findAvailableParkingSpots(parkingId, pageable)
    }

    @PutMapping("/{id}/occupy")
    fun occupyParkingSpot(@PathVariable id: UUID): ParkingSpotResponse {
        return parkingSpotService.occupyParkingSpot(id)
    }

    @PutMapping("/{id}/free")
    fun freeParkingSpot(@PathVariable id: UUID): ParkingSpotResponse {
        return parkingSpotService.freeParkingSpot(id)
    }
}
