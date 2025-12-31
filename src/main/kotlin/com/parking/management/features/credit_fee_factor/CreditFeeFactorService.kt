package com.parking.management.features.credit_fee_factor

import com.parking.management.comman.models.ConflictException
import com.parking.management.comman.models.Message
import com.parking.management.comman.models.NotFoundException
import com.parking.management.features.credit_fee_factor.models.CreditFeeFactorCreate
import com.parking.management.features.credit_fee_factor.models.CreditFeeFactorMapper
import com.parking.management.features.credit_fee_factor.models.CreditFeeFactorResponse
import com.parking.management.features.credit_fee_factor.models.CreditFeeFactorUpdate
import com.parking.management.features.credit_fee_factor.models.merge
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class CreditFeeFactorService(
    val repository: CreditFeeFactorRepository
) {
    @Transactional
    fun create(request: CreditFeeFactorCreate): CreditFeeFactorResponse {
        // Since unit is unique, this will throw an exception if it already exists.
        // A more graceful approach could be to check first, but this is fine for now.
        try {
            val creditFeeFactor = CreditFeeFactorMapper.toEntity(request)
            return CreditFeeFactorMapper.toResponse(repository.save(creditFeeFactor))
        } catch (e: DataIntegrityViolationException) {
            throw ConflictException("A credit fee factor for unit '${request.unit}' already exists.")
        }
    }

    @Transactional
    fun update(id: UUID, request: CreditFeeFactorUpdate): CreditFeeFactorResponse {
        val creditFeeFactor = repository.findById(id).orElseThrow { NotFoundException("$id credit fee factor not exists") }
        creditFeeFactor.merge(request)
        return CreditFeeFactorMapper.toResponse(repository.save(creditFeeFactor))
    }
    
    @Transactional(readOnly = true)
    fun getById(id: UUID): CreditFeeFactorResponse {
        return CreditFeeFactorMapper.toResponse(repository.findById(id).orElseThrow { NotFoundException("$id credit fee factor not exists") })
    }

    @Transactional(readOnly = true)
    fun getByUnit(unit: CreditUnit): CreditFeeFactorResponse {
        val creditFeeFactor = repository.findByUnitAndDeletedAtIsNull(unit).orElseThrow { NotFoundException("Credit fee factor for unit '$unit' not found") }
        return CreditFeeFactorMapper.toResponse(creditFeeFactor)
    }

    @Transactional(readOnly = true)
    fun getAll(pageable: Pageable, deletedIncluded: Boolean = false): Page<CreditFeeFactorResponse> {
        return if (deletedIncluded) repository.findAll(pageable).map { CreditFeeFactorMapper.toResponse(it) } else repository.findAllByDeletedAtIsNull(pageable).map { CreditFeeFactorMapper.toResponse(it) }
    }

    @Transactional
    fun delete(id: UUID): Message {
        val creditFeeFactor = repository.findById(id).orElseThrow { NotFoundException("$id credit fee factor not exists") }
        creditFeeFactor.deletedAt = LocalDateTime.now()
        repository.save(creditFeeFactor)
        return Message("Credit Fee Factor deleted successfully")
    }
}
