package com.parking.management.features.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.parking.management.comman.models.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
@Transactional
class FilterableService(
    private val repository: FilterRepository,
    private val objectMapper: ObjectMapper
) {
    fun getAll() = repository.findAll().map { FilterMapper.toResponse(it) }

    fun getById(id: String) = FilterMapper.
    toResponse(repository.findById(id).
    orElseThrow { RuntimeException("Filterable not found") })

    fun create(filterable: Filterable) = FilterMapper.toResponse(repository.save(filterable))

    fun getByResourceName(resourceName: String)
         = repository.findAllByTableName(resourceName).map { FilterMapper.toResponse(it) }.firstOrNull() ?: throw NotFoundException("Filterable not found for resource $resourceName")

    fun refresh(): List<FilterableResponseDto> {
        repository.deleteAll()
        /**
         * register filterables once
         */
        try {
            val resource = ClassPathResource("DefaultFilters.json")
            val inputStream = resource.inputStream
            // Parse JSON to List<Filterable>
            val filterables: List<Filterable> = objectMapper.readValue(inputStream, objectMapper.typeFactory.constructCollectionType(List::class.java, Filterable::class.java))
            // Save all filterables to MongoDB
            val savedFilterables = repository.saveAll(filterables)
            println("Successfully registered ${savedFilterables.size} filterables:")
            savedFilterables.forEach { filterable ->
                val totalProps = filterable.intProperties.size + filterable.stringProperties.size + filterable.enumProperties.size
                println("   - ${filterable.tableName} (${totalProps} properties)")
            }
        } catch (e: Exception) {
            println("Error registering filterables: ${e.message}")
            throw RuntimeException("Error registering filterables ", e)
        }
        return repository.findAll().map { FilterMapper.toResponse(it) }
    }

    fun delete(id: String) {
        val filterable = repository.findById(id).orElseThrow { RuntimeException("Filterable not found") }
        return repository.delete(filterable)
    }

}