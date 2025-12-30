package com.parking.management.features.parking_spot

import com.parking.management.features.parking.Parking
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "parking_spots")
data class ParkingSpot(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "parking_id", nullable = false)
    var parking: Parking,

    @Column(nullable = false)
    var level: String,

    @Column(nullable = false)
    var spotNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: ParkingSpotType,

    @Column(nullable = false)
    var isOccupied: Boolean = false,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = LocalDateTime.now()
)

enum class ParkingSpotType {
    CAR,
    EV,
    HANDICAP
}