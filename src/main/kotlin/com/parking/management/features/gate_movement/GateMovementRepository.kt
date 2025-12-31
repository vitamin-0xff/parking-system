package com.parking.management.features.gate_movement

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GateMovementRepository : MongoRepository<GateMovement, String> {
    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<GateMovement>
}

