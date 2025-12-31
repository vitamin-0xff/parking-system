package com.parking.management.features.credit_suppling

import com.parking.management.features.card.Card
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "credit_supplings")
data class CreditSuppling(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    var card: Card,

    @Column(nullable = false)
    var amount: Float,

    @Column(nullable = false)
    var feeTaken: Float,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var source: CreditSupplingSource,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: CreditSupplingStatus,

    @Column(nullable = false)
    var balanceBefore: Float,

    @Column(nullable = false)
    var balanceAfter: Float,

    @Column(nullable = false, unique = true)
    var reference: String,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "deleted_at", nullable = true)
    var deletedAt: LocalDateTime? = null
)

enum class CreditSupplingSource {
    ADMIN,
    ONLINE
}

enum class CreditSupplingStatus {
    SUCCESS,
    FAILED
}
