package com.parking.management.features.credit_fee_factor

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface CreditFeeFactorRepository : JpaRepository<CreditFeeFactor, UUID> {
    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<CreditFeeFactor>
    fun findByUnitAndDeletedAtIsNull(unit: CreditUnit): Optional<CreditFeeFactor>
}
