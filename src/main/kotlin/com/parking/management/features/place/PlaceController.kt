package com.parking.management.features.place

import com.parking.management.comman.models.Message
import com.parking.management.features.place.models.PlaceCreate
import com.parking.management.features.place.models.PlaceResponse
import com.parking.management.features.place.models.PlaceUpdate
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
@RequestMapping("/v1/api/places")
class PlaceController(
    val placeService: PlaceService
) {
    @PostMapping
    fun create(@Valid @RequestBody placeCreate: PlaceCreate): PlaceResponse {
        return placeService.create(placeCreate)
    }

    @PostMapping("/all")
    fun createList(@Valid @RequestBody placesCreate: List<PlaceCreate>): List<PlaceResponse> {
        return placeService.createList(placesCreate)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): PlaceResponse {
        return placeService.getById(id)
    }

    @GetMapping
    fun getAll(
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable): Page<PlaceResponse> {
        return placeService.getAll(pageable)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody placeUpdate: PlaceUpdate): PlaceResponse {
        return placeService.update(id, placeUpdate)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): Message {
        return placeService.delete(id)
    }

    @GetMapping("/city/{cityId}")
    fun getByCityId(
        @PathVariable cityId: UUID,
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable): Page<PlaceResponse> {
        return placeService.findAllByCityId(cityId, pageable)
    }
}
