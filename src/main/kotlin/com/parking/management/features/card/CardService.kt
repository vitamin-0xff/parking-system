package com.parking.management.features.card

import com.example.parking.client.api.ClientMapper
import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.features.card.models.CardCreate
import com.parking.management.features.card.models.CardMapper
import com.parking.management.features.card.models.CardResponse
import com.parking.management.features.card.models.CardUpdate
import com.parking.management.features.card.models.merge
import com.parking.management.features.client.ClientRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

class CardService(
    val repository: CardRepository,
    val clientRepository: ClientRepository
) {
    fun create(cardCreate: CardCreate): CardResponse {
        val client = clientRepository.findById(cardCreate.userId).orElseThrow { NotFoundException("${cardCreate.userId} client not exists") }
        val card = CardMapper.toEntity(cardCreate, client)
        return CardMapper.toResponse(repository.save(card), ClientMapper.toResponse(client))
    }

    fun createList(cardsCreate: List<CardCreate>): List<CardResponse> {
        val cards = cardsCreate.map {
            val client = clientRepository.findById(it.userId).orElseThrow { NotFoundException("${it.userId} client not exists") }
            CardMapper.toEntity(it, client)
        }
        return repository.saveAll(cards).map { CardMapper.toResponse(it, ClientMapper.toResponse(it.client)) }
    }

    @Transactional(readOnly = true)
    fun getById(uuid: UUID): CardResponse {
        val card = repository.findById(uuid).orElseThrow { NotFoundException("$uuid card not exists") }
        return CardMapper.toResponse(card, ClientMapper.toResponse(card.client))
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable, deletedIncluded: Boolean = false): Page<CardResponse> {
        if (!deletedIncluded)
            return repository.findAllByDeletedAtIsNull(pageable).map { CardMapper.toResponse(it, ClientMapper.toResponse(it.client)) }
        return repository.findAll(pageable).map { CardMapper.toResponse(it, ClientMapper.toResponse(it.client)) }
    }

    fun update(cardId: UUID, cardUpdate: CardUpdate): CardResponse {
        val card = repository.findById(cardId).orElseThrow { NotFoundException("$cardId card not exists") }
        card.merge(cardUpdate)
        return CardMapper.toResponse(repository.save(card), ClientMapper.toResponse(card.client))
    }

    fun delete(cardId: UUID): Message {
        val card = repository.findById(cardId).orElseThrow { NotFoundException("$cardId card not exists") }
        card.deletedAt = LocalDateTime.now()
        repository.save(card)
        return Message("card deleted successfully")
    }
}