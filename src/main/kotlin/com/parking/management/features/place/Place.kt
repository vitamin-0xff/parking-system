package com.parking.management.features.place

import com.parking.management.features.city.City
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "places")
data class Place(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var addressLine: String,

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    var city: City,

    @Column(nullable = false)
    var latitude: Float,

    @Column(nullable = false)
    var longitude: Float,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),
)
