package com.parking.management.features.parking_event

import com.parking.management.features.card.Card
import com.parking.management.features.entry_gate.EntryGate
import com.parking.management.features.parking.Parking
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ParkingEventRepository : JpaRepository<ParkingEvent, UUID> {
    /* all (deleted not deleted) */
    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<ParkingEvent>
    fun findAllByDeletedAtIsNotNull(pageable: Pageable): Page<ParkingEvent>

    /* by card (deleted not deleted) */
    fun findByCard(card: Card, pageable: Pageable): Page<ParkingEvent>
    fun findByCardAndDeletedAtIsNull(card: Card, pageable: Pageable): Page<ParkingEvent>
    fun findByCardAndDeletedAtIsNotNull(card: Card, pageable: Pageable): Page<ParkingEvent>

    /* by entry gate (deleted not deleted) */
    fun findByEntryGate(entryGate: EntryGate, pageable: Pageable): Page<ParkingEvent>
    fun findByEntryGateAndDeletedAtIsNull(entryGate: EntryGate, pageable: Pageable): Page<ParkingEvent>
    fun findByEntryGateAndDeletedAtIsNotNull(entryGate: EntryGate, pageable: Pageable): Page<ParkingEvent>

    /* by parking (deleted not deleted) */
    fun findByParking(parking: Parking, pageable: Pageable): Page<ParkingEvent>
    fun findByParkingAndDeletedAtIsNull(parking: Parking, pageable: Pageable): Page<ParkingEvent>
    fun findByParkingAndDeletedAtIsNotNull(parking: Parking, pageable: Pageable): Page<ParkingEvent>

    /* by direction (deleted not deleted) */
    fun findByDirection(direction: Direction, pageable: Pageable): Page<ParkingEvent>
    fun findByDirectionAndDeletedAtIsNull(direction: Direction, pageable: Pageable): Page<ParkingEvent>
    fun findByDirectionAndDeletedAtIsNotNull(direction: Direction, pageable: Pageable): Page<ParkingEvent>

}
