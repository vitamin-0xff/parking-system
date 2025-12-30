package com.parking.management.features.card

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CardRepository : JpaRepository<Card, UUID> {
    fun findByCardNumber(cardNumber: String): Card?
    fun findAllByClientId(clientId: UUID, pageable: Pageable): Page<Card>
    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<Card>
}
