package com.parking.management.features.entry_gate

import com.parking.management.comman.models.Message
import com.parking.management.features.entry_gate.models.EntryGateCreate
import com.parking.management.features.entry_gate.models.EntryGateResponse
import com.parking.management.features.entry_gate.models.EntryGateSpecificationsDto
import com.parking.management.features.entry_gate.models.EntryGateUpdate
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
@RequestMapping("/v1/api/entry-gates")
class EntryGateController(
    val entryGateService: EntryGateService
) {
    @PostMapping
    fun create(@Valid @RequestBody entryGateCreate: EntryGateCreate): EntryGateResponse {
        return entryGateService.create(entryGateCreate)
    }

    @PostMapping("/all")
    fun createList(@Valid @RequestBody entryGatesCreate: List<EntryGateCreate>): List<EntryGateResponse> {
        return entryGateService.createList(entryGatesCreate)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): EntryGateResponse {
        return entryGateService.getById(id)
    }

    @GetMapping
    fun getAll(
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable): Page<EntryGateResponse> {
        return entryGateService.getAll(pageable)
    }

    @PostMapping("/instances/all")
    fun getAllFilter(
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable,
        @RequestBody filter: EntryGateSpecificationsDto? = null
    ): Page<EntryGateResponse> {
        return entryGateService.getAllFilter(pageable, filter)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody entryGateUpdate: EntryGateUpdate): EntryGateResponse {
        return entryGateService.update(id, entryGateUpdate)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): Message {
        return entryGateService.delete(id)
    }

    @GetMapping("/parking/{parkingId}")
    fun getByParkingId(
        @PathVariable parkingId: UUID,
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable): Page<EntryGateResponse> {
        return entryGateService.findAllByParkingId(parkingId, pageable)
    }
}
