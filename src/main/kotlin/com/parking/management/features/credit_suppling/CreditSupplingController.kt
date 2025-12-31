package com.parking.management.features.credit_suppling

import com.parking.management.comman.models.Message
import com.parking.management.features.card.CardRepository
import com.parking.management.features.credit_suppling.models.CreditSupplingCreate
import com.parking.management.features.credit_suppling.models.CreditSupplingResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/credit-supplings")
class CreditSupplingController(
    private val creditSupplingService: CreditSupplingService,
) {

    @PostMapping
    fun createCreditSuppling(@Valid @RequestBody request: CreditSupplingCreate): CreditSupplingResponse {
        return creditSupplingService.create(request)
    }

    @PostMapping("/all")
    fun createListCreditSuppling(@Valid @RequestBody request: List<CreditSupplingCreate>): List<CreditSupplingResponse> {
        return creditSupplingService.createList(request)
    }

    @GetMapping("/{id}")
    fun getCreditSupplingById(@PathVariable id: UUID): CreditSupplingResponse {
        return creditSupplingService.getById(id)
    }

    @GetMapping
    fun getAllCreditSupplings(
        pageable: Pageable,
        @RequestParam(required = false, defaultValue = "false") deletedIncluded: Boolean
    ): Page<CreditSupplingResponse> {
        return creditSupplingService.getAll(pageable, deletedIncluded)
    }

    @DeleteMapping("/{id}")
    fun deleteCreditSuppling(@PathVariable id: UUID): Message {
        return creditSupplingService.delete(id)
    }
}
