package com.parking.management.features.parking

import com.parking.management.features.city.City
import com.parking.management.features.place.Place
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "parkings")
data class Parking(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    var city: City,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var latitude: Float,

    @Column(nullable = false)
    var longitude: Float,

    @Column(nullable = false)
    var totalCapacity: Int,

    @Column(nullable = false)
    var currentOccupied: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ParkingStatus,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),
)

enum class ParkingStatus {
    OPEN,
    CLOSED,
    MAINTENANCE
}
