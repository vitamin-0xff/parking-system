package com.parking.management.features.country

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "countries")
data class Country(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false, unique = true)
    var isoCode: String,

    @Column
    var latitude: Double,

    @Column
    var longitude: Double,

    @Column
    var zoomFactor: UInt,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(nullable = false, updatable = false)
    var updateAt: LocalDateTime? = LocalDateTime.now()
)
