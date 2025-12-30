package com.parking.management.features.client

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "clients"
)
data class Client(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    var fullName: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var lastName: String,

    @Column(unique = true)
    var email: String? = null,

    @Column(nullable = false, unique = true)
    var phone: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ClientStatus = ClientStatus.ACTIVE,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: LocalDateTime?  = LocalDateTime.now(),

    @Column(name = "deactivated_at", nullable = true)
    var deactivatedAt: LocalDateTime? = null,

    @Column(name = "deleted_at", nullable = true)
    var deletedAt: LocalDateTime? = null
)

enum class ClientStatus {
    ACTIVE,
    BLOCKED
}