package com.parking.management.features.card

import com.parking.management.features.client.Client
import com.parking.management.features.user.User
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "cards")
data class Card(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    var client: Client,

    @Column(nullable = false, unique = true)
    var cardNumber: String,

    @Column(nullable = false)
    var creditBalance: Float,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: CardStatus,

    @Column(nullable = false)
    var issuedAt: LocalDateTime,

    @Column(nullable = false)
    var expiresAt: LocalDateTime,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = LocalDateTime.now(),

    @Column(nullable = false)
    var deletedAt: LocalDateTime? = null,
)

enum class CardStatus {
    ACTIVE,
    BLOCKED,
    EXPIRED
}
