package com.parking.management.features.city

import com.parking.management.features.country.Country
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "cities")
data class City(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var postalCode: String,

    @Column(nullable = false)
    var stateCode: String,

    @Column
    var latitude: Double,

    @Column
    var longitude: Double,

    @Column
    var zoomFactor: Int,

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    var country: Country,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),
)
