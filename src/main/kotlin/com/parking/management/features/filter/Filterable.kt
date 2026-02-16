package com.parking.management.features.filter

import jakarta.persistence.Id
import jakarta.validation.constraints.NotNull
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

/**
 * sub classes for distinctions only, except for the enum one, which has an extra field
 */
open class Property(
    val prettyName: String,
    val name: String,
    val description: String? = null,
    val databasePath: String? = null
)

class StringProperty(
    prettyName: String,
    name: String,
    description: String? = null,
    databasePath: String? = null
): Property(
    prettyName,
    name,
    description,
    databasePath
)

class IntProperty(
    prettyName: String,
    name: String,
    description: String? = null,
    databasePath: String? = null
): Property(
    prettyName,
    name,
    description,
    databasePath
)

class DateProperties(
    prettyName: String,
    name: String,
    description: String? = null,
    databasePath: String? = null
): Property(
    prettyName,
    name,
    description,
    databasePath
)

class EnumProperty(
    prettyName: String,
    name: String,
    val enumClassFullName: String,
    description: String? = null,
    databasePath: String? = null
): Property(
    prettyName,
    name,
    description,
    databasePath
)

@Document(collection = "filterables")
data class Filterable(
    @Id
    val id: String? = null,
    @NotNull
    @Indexed(unique = true)
    var tableName: String,
    val intProperties: List<IntProperty>,
    val stringProperties: List<StringProperty>,
    val enumProperties: List<EnumProperty>,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)