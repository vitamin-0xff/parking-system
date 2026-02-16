package com.parking.management.features.runnables

import com.fasterxml.jackson.databind.ObjectMapper
import com.parking.management.features.filter.Filterable
import com.parking.management.features.filter.FilterRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.core.Ordered
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class RegisterFilterables(
    val filterRepository: FilterRepository,
    val objectMapper: ObjectMapper
): CommandLineRunner, Ordered {
    override fun getOrder(): Int = 9999
    override fun run(vararg args: String) {
        /**
         * register filterables once
         */
        try {
            // Check if filterables are already registered
            val existingCount = filterRepository.count()
            if (existingCount > 0) {
                println("Filterables already registered (count: $existingCount)")
                return
            }
            println("Registering filterables from DefaultFilters.json...")
            // Read the JSON file from resources
            val resource = ClassPathResource("DefaultFilters.json")
            val inputStream = resource.inputStream
            // Parse JSON to List<Filterable>
            val filterables: List<Filterable> = objectMapper.readValue(inputStream, objectMapper.typeFactory.constructCollectionType(List::class.java, Filterable::class.java))
            // Save all filterables to MongoDB
            val savedFilterables = filterRepository.saveAll(filterables)
            println("Successfully registered ${savedFilterables.size} filterables:")
            savedFilterables.forEach { filterable ->
                val totalProps = filterable.intProperties.size + filterable.stringProperties.size + filterable.enumProperties.size
                println("   - ${filterable.tableName} ($totalProps properties)")
            }
        } catch (e: Exception) {
            println("Error registering filterables: ${e.message}")
            e.printStackTrace()
        }
    }
}