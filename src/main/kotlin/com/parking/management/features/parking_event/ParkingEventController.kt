package com.parking.management.features.parking_event


import com.parking.management.comman.models.Message
import com.parking.management.features.parking_event.models.ParkingEventCreate
import com.parking.management.features.parking_event.models.ParkingEventResponse
import jakarta.validation.Valid
import jakarta.websocket.server.PathParam
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/api/parkingEvents")
class ParkingEventController(
    val parkingEventService: ParkingEventService
) {
    @PostMapping
    fun create(@Valid @RequestBody parkingEventCreate: ParkingEventCreate): ParkingEventResponse {
        return parkingEventService.create(parkingEventCreate)
    }

    @PostMapping("/all")
    fun createList(@Valid parkingEventsCreate: List<ParkingEventCreate>): List<ParkingEventResponse> {
        return parkingEventService.createList(parkingEventsCreate)
    }

    @GetMapping("/{id}")
    fun get(@PathParam("id") id: UUID): ParkingEventResponse {
        return parkingEventService.getById(id)
    }

    @GetMapping("/all")
    fun getAll(
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable): Page<ParkingEventResponse> {
        return parkingEventService.getAll(pageable)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathParam("id") id: UUID): Message {
        return parkingEventService.delete(id)
    }
}