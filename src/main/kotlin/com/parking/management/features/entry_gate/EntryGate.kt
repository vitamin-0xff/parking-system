package com.parking.management.features.entry_gate

import com.parking.management.features.parking.Parking
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "entry_gates")
data class EntryGate(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "parking_id", nullable = false)
    var parking: Parking,

    @Column(nullable = false)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var direction: EntryGateDirection,

    @Column(nullable = false)
    var hardwareId: String,

    @Column(nullable = false)
    var isActive: Boolean,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),
)

enum class EntryGateDirection {
    IN,
    OUT
}
