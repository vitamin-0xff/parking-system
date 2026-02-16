package com.parking.management.features.filter

import java.time.LocalDateTime

open class ConcreteFilterDto(
    val prettyName: String,
    val name: String,
    val description: String? = null,
    val path: String? = null,
)

class FilterableIntRangePropertyDto constructor(
    prettyName: String,
    name: String,
    val max: Int,
    val min: Int,
    description: String? = null,
    path: String? = null
    ): ConcreteFilterDto(
    prettyName,
    name,
    description,
    path,
)

class FilterableListStringsPropertyDto(
    prettyName: String,
    name: String,
    val possibleValues: List<String>,
    description: String? = null,
    path: String? = null
): ConcreteFilterDto(
    prettyName,
    name,
    description,
    path,
)

class FilterableDateRangePropertyDto(
    prettyName: String,
    name: String,
    val max: LocalDateTime,
    val min: LocalDateTime,
    description: String? = null,
    path: String? = null
): ConcreteFilterDto(
    prettyName,
    name,
    description,
    path
)
