package com.parking.management.features.client

import com.example.parking.client.api.ClientCreate
import com.example.parking.client.api.ClientMapper
import com.example.parking.client.api.ClientResponse
import com.example.parking.client.api.ClientUpdate
import com.parking.management.comman.models.Message
import jakarta.validation.Valid
import jakarta.websocket.server.PathParam
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/api/clients")
class ClientController(
    val clientService: ClientService
) {
    @PostMapping
    fun create(@Valid @RequestBody clientCreate: ClientCreate): ClientResponse {
        return clientService.create(clientCreate)
    }

    @PostMapping("/all")
    fun createList(@Valid clientsCreate: List<ClientCreate>): List<ClientResponse> {
        return clientService.createList(clientsCreate)
    }

    @GetMapping("/{id}")
    fun get(@PathParam("id") id: UUID): ClientResponse {
        return clientService.getById(id)
    }

    @GetMapping("/all")
    fun getAll(
        @PageableDefault(
        size = 20,
        page = 1) pageable: Pageable): Page<ClientResponse> {
        return clientService.getAll(pageable)
    }

    @PutMapping("/{id}")
    fun update(@PathParam("id") id: UUID, @Valid @RequestBody clientUpdate: ClientUpdate): ClientResponse {
        return clientService.update(id, clientUpdate)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathParam("id") id: UUID): Message {
        return clientService.delete(id)
    }
}