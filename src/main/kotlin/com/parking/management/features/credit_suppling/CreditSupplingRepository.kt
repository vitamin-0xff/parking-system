package com.parking.management.features.credit_suppling

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CreditSupplingRepository : JpaRepository<CreditSuppling, UUID> {
    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<CreditSuppling>
}
