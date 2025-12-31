package com.parking.management.features.gate_movement

import com.parking.management.comman.models.Message
import com.parking.management.features.gate_movement.models.GateMovementCreate
import com.parking.management.features.gate_movement.models.GateMovementResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/gate-movements")
class GateMovementController(private val gateMovementService: GateMovementService) {

    @PostMapping
    fun createGateMovement(@Valid @RequestBody request: GateMovementCreate): GateMovementResponse {
        val gateMovementResponse = gateMovementService.create(request)
        return gateMovementResponse
    }

    @PostMapping("/all")
    fun createListGateMovement(@Valid @RequestBody request: List<GateMovementCreate>): List<GateMovementResponse> {
        val gateMovementResponse = gateMovementService.createList(request)
        return gateMovementResponse
    }

    @GetMapping("/{id}")
    fun getGateMovementById(@PathVariable id: String): GateMovementResponse {
        return gateMovementService.getById(id)
    }

    @GetMapping
    fun getAllGateMovements(
        pageable: Pageable,
        @RequestParam(required = false, defaultValue = "false") deletedIncluded: Boolean
    ): Page<GateMovementResponse> {
        return gateMovementService.getAll(pageable, deletedIncluded)
    }

    @DeleteMapping("/{id}")
    fun deleteGateMovement(@PathVariable id: String): Message {
        return gateMovementService.delete(id)
    }
}
