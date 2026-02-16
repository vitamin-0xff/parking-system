package com.parking.management.features.filter

import java.time.LocalDateTime

data class IntPropertyResponseDto(
    val prettyName: String,
    val name: String,
    val description: String? = null,
    val databasePath: String? = null,
)

data class StringPropertyResponseDto(
    val prettyName: String,
    val name: String,
    val description: String? = null,
    val databasePath: String? = null,
)

data class EnumPropertyResponseDto(
    val prettyName: String,
    val name: String,
    val description: String? = null,
    val databasePath: String? = null,
    val enumClassFullName: String? = null,
)

data class FilterableResponseDto(
    val id: String?,
    val whoIsMe: String,
    val intProperties: List<IntPropertyResponseDto>,
    val stringProperties: List<StringPropertyResponseDto>,
    val enumProperties: List<EnumPropertyResponseDto>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

object FilterMapper {
    fun toResponse(filterable: Filterable) = FilterableResponseDto(
        id = filterable.id,
        whoIsMe = filterable.tableName,
        intProperties = buildIntPropertiesResponse(filterable),
        stringProperties = buildStringPropertiesResponse(filterable),
        enumProperties = buildEnumPropertiesResponse(filterable),
        createdAt = filterable.createdAt,
        updatedAt = filterable.updatedAt
    )

    private fun buildIntPropertiesResponse(filterable: Filterable): List<IntPropertyResponseDto> {
        return filterable.intProperties.map { prop ->
            IntPropertyResponseDto(
                prettyName = prop.prettyName,
                name = prop.name,
                description = prop.description,
                databasePath = prop.databasePath
            )
        }
    }

    private fun buildStringPropertiesResponse(filterable: Filterable): List<StringPropertyResponseDto> {
        return filterable.stringProperties.map { prop ->
            StringPropertyResponseDto(
                prettyName = prop.prettyName,
                name = prop.name,
                description = prop.description,
                databasePath = prop.databasePath
            )
        }
    }

    private fun buildEnumPropertiesResponse(filterable: Filterable): List<EnumPropertyResponseDto> {
        return filterable.enumProperties.map { prop ->
            EnumPropertyResponseDto(
                prettyName = prop.prettyName,
                name = prop.name,
                description = prop.description,
                databasePath = prop.databasePath,
                enumClassFullName = prop.enumClassFullName
            )
        }
    }
}


