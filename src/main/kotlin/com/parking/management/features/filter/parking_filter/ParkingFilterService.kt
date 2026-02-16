package com.parking.management.features.filter.parking_filter

import com.parking.management.features.filter.FilterRepository
import com.parking.management.features.filter.Property
import com.parking.management.features.filter.IntProperty
import com.parking.management.features.filter.StringProperty
import com.parking.management.features.filter.EnumProperty
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.From
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Root
import org.springframework.stereotype.Service

data class MinMax(
    val min: Int,
    val max: Int
)

enum class FilterType {
    INT,
    STRING,
    ENUM
}

// Concrete Filter Domain Models
sealed class ConcreteFilter(open val property: Property, val type: FilterType)

data class ConcreteIntFilter(
    override val property: IntProperty,
    val minMax: MinMax
) : ConcreteFilter(property, FilterType.INT)

data class ConcreteStringFilter(
    override val property: StringProperty,
    val availableValues: List<String>
) : ConcreteFilter(property, FilterType.STRING)

data class ConcreteEnumFilter(
    override val property: EnumProperty,
    val availableValues: List<String>
) : ConcreteFilter(property, FilterType.ENUM)

// Response DTOs for API
data class ConcreteIntFilterResponseDto(
    val prettyName: String,
    val name: String,
    val description: String?,
    val databasePath: String?,
    val minMax: MinMax
)

data class ConcreteStringFilterResponseDto(
    val prettyName: String,
    val name: String,
    val description: String?,
    val databasePath: String?,
    val availableValues: List<String>
)

data class ConcreteEnumFilterResponseDto(
    val prettyName: String,
    val name: String,
    val description: String?,
    val databasePath: String?,
    val enumClassFullName: String?,
    val availableValues: List<String>
)

data class ConcreteFiltersResponseDto(
    val intFilters: List<ConcreteIntFilterResponseDto>,
    val stringFilters: List<ConcreteStringFilterResponseDto>,
    val enumFilters: List<ConcreteEnumFilterResponseDto>
)

object ConcreteFilterMapper {
    fun toResponse(concreteFilters: List<ConcreteFilter>): ConcreteFiltersResponseDto {
        val intFilters = mutableListOf<ConcreteIntFilterResponseDto>()
        val stringFilters = mutableListOf<ConcreteStringFilterResponseDto>()
        val enumFilters = mutableListOf<ConcreteEnumFilterResponseDto>()

        concreteFilters.forEach { filter ->
            when (filter) {
                is ConcreteIntFilter -> {
                    intFilters.add(
                        ConcreteIntFilterResponseDto(
                            prettyName = filter.property.prettyName,
                            name = filter.property.name,
                            description = filter.property.description,
                            databasePath = filter.property.databasePath,
                            minMax = filter.minMax
                        )
                    )
                }
                is ConcreteStringFilter -> {
                    stringFilters.add(
                        ConcreteStringFilterResponseDto(
                            prettyName = filter.property.prettyName,
                            name = filter.property.name,
                            description = filter.property.description,
                            databasePath = filter.property.databasePath,
                            availableValues = filter.availableValues
                        )
                    )
                }
                is ConcreteEnumFilter -> {
                    enumFilters.add(
                        ConcreteEnumFilterResponseDto(
                            prettyName = filter.property.prettyName,
                            name = filter.property.name,
                            description = filter.property.description,
                            databasePath = filter.property.databasePath,
                            enumClassFullName = filter.property.enumClassFullName,
                            availableValues = filter.availableValues
                        )
                    )
                }
            }
        }

        return ConcreteFiltersResponseDto(
            intFilters = intFilters,
            stringFilters = stringFilters,
            enumFilters = enumFilters
        )
    }
}

@Service
class FilterRangeUniquesResolverService(
    val filterRepository: FilterRepository,
    val entityManager: EntityManager
) {
    fun <T> getConcreteFilters(resourceName: String, classT: Class<T>): List<ConcreteFilter> {
        val filter = filterRepository.findAllByTableName(resourceName).firstOrNull()
            ?: throw RuntimeException("No filters found for resource $resourceName")

        val concreteFilters = mutableListOf<ConcreteFilter>()

        // Handle IntProperties
        filter.intProperties.forEach { property ->
            val minMax = getMaxMinOfProperties(property.databasePath ?: property.name, classT)
            concreteFilters.add(ConcreteIntFilter(property, minMax = minMax))
        }

        // Handle StringProperties
        filter.stringProperties.forEach { property ->
            val values = getDistinctValues(property.databasePath ?: property.name, classT)
            concreteFilters.add(ConcreteStringFilter(property, availableValues = values))
        }

        // Handle EnumProperties
        filter.enumProperties.forEach { property ->
            val enumValues = getEnumValues(property.enumClassFullName)
            concreteFilters.add(ConcreteEnumFilter(property, availableValues = enumValues))
        }

        return concreteFilters
    }

    fun <T> getConcreteFiltersResponse(resourceName: String, classT: Class<T>): ConcreteFiltersResponseDto {
        val concreteFilters = getConcreteFilters(resourceName, classT)
        return ConcreteFilterMapper.toResponse(concreteFilters)
    }

    fun getEnumValues(enumClassFullName: String): List<String> {
        return try {
            @Suppress("UNCHECKED_CAST")
            val enumClass = Class.forName(enumClassFullName) as Class<Enum<*>>
            enumClass.enumConstants?.map { it.name } ?: emptyList()
        } catch (e: Exception) {
            println("Error loading enum class $enumClassFullName: ${e.message}")
            emptyList()
        }
    }

    fun <T> getMaxMinOfProperties(attributeName: String, classT: Class<T>): MinMax {
        try {
            val cb: CriteriaBuilder = entityManager.criteriaBuilder
            val query: CriteriaQuery<Array<Any>> = cb.createQuery(Array<Any>::class.java)
            val root: Root<T> = query.from(classT)
            val path = resolve<Int>(root, attributeName.split("."))
            // Select max and min
            query.multiselect(
                cb.max(path as Expression<Int>),
                cb.min(path as Expression<Int>)
            )

            val result = entityManager.createQuery(query).singleResult

            val max = (result[0] as? Number)?.toInt() ?: 0
            val min = (result[1] as? Number)?.toInt() ?: 0

            return MinMax(min = min, max = max)
        } catch (e: Exception) {
            println("Error getting min/max for attribute $attributeName: ${e.message}")
            return MinMax(min = 0, max = 0)
        }
    }

    fun <T> getDistinctValues(attributeName: String, classT: Class<T>): List<String> {
        try {
            val cb: CriteriaBuilder = entityManager.criteriaBuilder
            val query: CriteriaQuery<String> = cb.createQuery(String::class.java)
            val root: Root<T> = query.from(classT)
            val path = resolve<String>(root, attributeName.split("."))
            query.select(path).distinct(true)
            return entityManager.createQuery(query).resultList.filterNotNull()
        } catch (e: Exception) {
            println("Error getting distinct values for attribute $attributeName: ${e.message}")
            return emptyList()
        }
    }

    fun <T> resolve(root: From<*, *>, parts: List<String>): Path<T> {
        var from = root
        for (p in parts.dropLast(1)) {
            from = from.join<Any, Any>(p)
        }
        return from.get<T>(parts.last())
    }
}