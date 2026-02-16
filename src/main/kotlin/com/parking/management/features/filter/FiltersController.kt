package com.parking.management.features.filter

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/filters")
class FiltersController(
    val service: FilterableService
) {
    @GetMapping("/")
    fun getAll() = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String) = service.getById(id)

    @GetMapping("/r/{resourceName}")
    fun getByResourceName(@PathVariable resourceName: String) = service.getByResourceName(resourceName)

    @PutMapping("/refresh")
    fun refresh() = service.refresh()
}