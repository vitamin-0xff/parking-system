package com.parking.management.features.credit_fee_factor

import com.parking.management.comman.models.Message
import com.parking.management.features.credit_fee_factor.models.CreditFeeFactorCreate
import com.parking.management.features.credit_fee_factor.models.CreditFeeFactorResponse
import com.parking.management.features.credit_fee_factor.models.CreditFeeFactorUpdate
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/credit-fee-factors")
class CreditFeeFactorController(private val creditFeeFactorService: CreditFeeFactorService) {

    @PostMapping
    fun createCreditFeeFactor(@Valid @RequestBody request: CreditFeeFactorCreate): CreditFeeFactorResponse {
        return creditFeeFactorService.create(request)
    }

    @PatchMapping("/{id}")
    fun updateCreditFeeFactor(@PathVariable id: UUID, @Valid @RequestBody request: CreditFeeFactorUpdate): CreditFeeFactorResponse {
        return creditFeeFactorService.update(id, request)
    }

    @GetMapping("/{id}")
    fun getCreditFeeFactorById(@PathVariable id: UUID): CreditFeeFactorResponse {
        return creditFeeFactorService.getById(id)
    }

    @GetMapping("/unit/{unit}")
    fun getCreditFeeFactorByUnit(@PathVariable unit: CreditUnit): CreditFeeFactorResponse {
        return creditFeeFactorService.getByUnit(unit)
    }

    @GetMapping
    fun getAllCreditFeeFactors(
        pageable: Pageable,
        @RequestParam(required = false, defaultValue = "false") deletedIncluded: Boolean
    ): Page<CreditFeeFactorResponse> {
        return creditFeeFactorService.getAll(pageable, deletedIncluded)
    }

    @DeleteMapping("/{id}")
    fun deleteCreditFeeFactor(@PathVariable id: UUID): Message {
        return creditFeeFactorService.delete(id)
    }
}
