package com.parking.management.features.parking_event

import com.parking.management.features.card.Card
import com.parking.management.features.entry_gate.EntryGate
import com.parking.management.features.parking.Parking
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "parking_events")
data class ParkingEvent(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    var card: Card,

    @ManyToOne
    @JoinColumn(name = "parking_id", nullable = false)
    var parking: Parking,

    @ManyToOne
    @JoinColumn(name = "entry_gate_id", nullable = false)
    var entryGate: EntryGate,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var direction: Direction,

    @Column(name = "charged_amount", nullable = false)
    var chargedCredits: Float,

    @Column(nullable = false)
    var timestamp: LocalDateTime = LocalDateTime.now(),

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = LocalDateTime.now(),

    @Column(nullable = false)
    var deletedAt: LocalDateTime? = null,
)

enum class Direction {
    IN,
    OUT
}
