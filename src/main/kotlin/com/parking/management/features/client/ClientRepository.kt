package com.parking.management.features.client

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ClientRepository : JpaRepository<Client, UUID> {

    fun findByEmail(email: String): Client?

    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<Client>

    fun existsByEmail(email: String): Boolean

    fun existsByPhone(phone: String): Boolean

    fun findByStatus(status: ClientStatus, pages: Pageable): Page<Client>
}