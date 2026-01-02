package com.parking.management.features.card


import com.parking.management.comman.models.Message
import com.parking.management.features.card.models.CardCreate
import com.parking.management.features.card.models.CardCreateV2
import com.parking.management.features.card.models.CardResponse
import com.parking.management.features.card.models.CardUpdate
import jakarta.validation.Valid
import jakarta.websocket.server.PathParam
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/api/cards")
class CardController(
    val cardService: CardService
) {
    @PostMapping
    fun create(@Valid @RequestBody cardCreate: CardCreate): CardResponse {
        return cardService.create(cardCreate)
    }

    @PostMapping("/v11")
    fun createV1(@Valid @RequestBody cardCreate: CardCreateV2): CardResponse {
        return cardService.createWithUser(cardCreate)
    }

    @PostMapping("/all")
    fun createList(@Valid cardsCreate: List<CardCreate>): List<CardResponse> {
        return cardService.createList(cardsCreate)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: UUID): CardResponse {
        return cardService.getById(id)
    }

    @GetMapping("/all")
    fun getAll(
        @PageableDefault(
            size = 20,
            page = 0) pageable: Pageable): Page<CardResponse> {
        return cardService.getAll(pageable)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: UUID, @Valid @RequestBody cardUpdate: CardUpdate): CardResponse {
        return cardService.update(id, cardUpdate)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: UUID): Message {
        return cardService.delete(id)
    }
}