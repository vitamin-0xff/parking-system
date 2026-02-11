package com.parking.management.features.parking

import com.fasterxml.jackson.databind.ObjectMapper
import com.parking.management.comman.models.Message
import com.parking.management.features.parking.models.ParkingCreate
import com.parking.management.features.parking.models.ParkingResponse
import com.parking.management.features.parking.models.ParkingUpdate
import com.parking.management.specifications.Filter
import com.parking.management.specifications.FilterObject
import com.parking.management.specifications.Filters
import com.parking.management.specifications.SpecificationsDto
import com.parking.management.specifications.SpecificationsType
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
@RequestMapping("/v1/api/parkings")
class ParkingController(
    val parkingService: ParkingService,
    val objectMapper: ObjectMapper
) {
    @PostMapping
    fun create(@Valid @RequestBody parkingCreate: ParkingCreate): ParkingResponse {
        return parkingService.create(parkingCreate)
    }

    @PostMapping("/all")
    fun createList(@Valid @RequestBody parkingsCreate: List<ParkingCreate>): List<ParkingResponse> {
        return parkingService.createList(parkingsCreate)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ParkingResponse {
        return parkingService.getById(id)
    }

    @PostMapping("/instances/all")
    fun getAll(
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable, @RequestBody filter: List<FilterObject>? = null): Page<ParkingResponse> {
        return parkingService.getAll(pageable, filter)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody parkingUpdate: ParkingUpdate): ParkingResponse {
        return parkingService.update(id, parkingUpdate)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): Message {
        return parkingService.delete(id)
    }

    @GetMapping("/place/{placeId}")
    fun getByPlaceId(
        @PathVariable placeId: UUID,
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable): Page<ParkingResponse> {
        return parkingService.findAllByPlaceId(placeId, pageable)
    }
}
