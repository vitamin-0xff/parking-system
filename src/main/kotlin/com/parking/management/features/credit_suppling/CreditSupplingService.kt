package com.parking.management.features.credit_suppling

import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.features.card.CardRepository
import com.parking.management.features.credit_suppling.models.CreditSupplingCreate
import com.parking.management.features.credit_suppling.models.CreditSupplingMapper
import com.parking.management.features.credit_suppling.models.CreditSupplingResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class CreditSupplingService(
    val repository: CreditSupplingRepository,
    val cardRepository: CardRepository
) {
    fun create(creditSupplingCreate: CreditSupplingCreate): CreditSupplingResponse {
        val card = cardRepository.findById(creditSupplingCreate.cardId).orElseThrow { NotFoundException("${creditSupplingCreate.cardId} card not exists") }
        // Potentially, here you could add logic to update the card's balance
        val creditSuppling = CreditSupplingMapper.toEntity(creditSupplingCreate, card)
        return CreditSupplingMapper.toResponse(repository.save(creditSuppling))
    }

    fun createList(creditSupplingCreate: List<CreditSupplingCreate>): List<CreditSupplingResponse> {
        return creditSupplingCreate.map { create(it) }
    }

    @Transactional(readOnly = true)
    fun getById(id: UUID): CreditSupplingResponse {
        return CreditSupplingMapper.toResponse(repository.findById(id).orElseThrow { NotFoundException("$id credit suppling not exists") })
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable, deletedIncluded: Boolean = false): Page<CreditSupplingResponse> {
        return if (deletedIncluded) repository.findAll(pageable).map { CreditSupplingMapper.toResponse(it) } else repository.findAllByDeletedAtIsNull(pageable).map { CreditSupplingMapper.toResponse(it) }
    }

    fun delete(id: UUID): Message {
        val creditSuppling = repository.findById(id).orElseThrow { NotFoundException("$id credit suppling not exists") }
        creditSuppling.deletedAt = LocalDateTime.now()
        repository.save(creditSuppling)
        return Message("Credit Suppling deleted successfully")
    }
}
