package com.parking.management.features.entry_gate

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EntryGateRepository : JpaRepository<EntryGate, UUID> {
    fun findByName(name: String): EntryGate?
    fun findAllByParkingId(parkingId: UUID, pageable: Pageable): Page<EntryGate>
}
