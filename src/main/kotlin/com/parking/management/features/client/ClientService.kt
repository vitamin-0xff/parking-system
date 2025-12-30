package com.parking.management.features.client

import com.example.parking.client.api.ClientCreate
import com.example.parking.client.api.ClientMapper
import com.example.parking.client.api.ClientResponse
import com.example.parking.client.api.ClientUpdate
import com.example.parking.client.api.merge
import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Transactional
@Service
class ClientService(
    val repository: ClientRepository,
) {

    fun create(clientCreate: ClientCreate): ClientResponse {
        val client = ClientMapper.toEntity(clientCreate)
        return ClientMapper.toResponse(repository.save(client))
    }

    fun createList(clientsCreate: List<ClientCreate>): List<ClientResponse> {
        val clients = clientsCreate.map { ClientMapper.toEntity(it) }
        return repository.saveAll(clients).map { ClientMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): ClientResponse {
        return ClientMapper.toResponse(repository.findById(uuid).orElseThrow { NotFoundException("$uuid Client not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable, deletedIncluded: Boolean = false): Page<ClientResponse> {
        if (!deletedIncluded)
            return repository.findAllByDeletedAtIsNull(pageable).map { ClientMapper.toResponse(it) }
        return repository.findAll(pageable).map { ClientMapper.toResponse(it) }
    }

    fun update(clientId: UUID, clientUpdate: ClientUpdate): ClientResponse {
        val client = repository.findById(clientId).orElseThrow { NotFoundException("$clientId Client not exists") }
        client.merge(clientUpdate)
        return ClientMapper.toResponse(repository.save(client))
    }

    fun delete(clientId: UUID): Message {
        val client = repository.findById(clientId).orElseThrow { NotFoundException("$clientId Client not exists") }
        client.deletedAt = LocalDateTime.now()
        return Message("Client deleted successfully")
    }

}