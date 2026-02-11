package com.parking.management.specifications

import java.time.LocalDateTime

/**
 * field in the package
 * filter logic:
 * filter[0].fieldName=name&
 * filter[0].value=John
 * filter[0].type=int
 */

sealed class SpecificationsDto

enum class SpecificationsType {
    INT, DATE, STRING
}

data class SpecificationsIntRangeDto(
    val fieldName: String,
    val start: Int,
    val end: Int? = null,
    val type: SpecificationsType = SpecificationsType.INT
): SpecificationsDto()

data class SpecificationsDateRangeDto(
    val fieldName: String,
    val start: LocalDateTime,
    val end: LocalDateTime? = null,
    val type: SpecificationsType = SpecificationsType.DATE
): SpecificationsDto()

data class SpecificationsStringListDto(
    val fieldName: String,
    val listOfStrings: List<String>,
    val type: SpecificationsType = SpecificationsType.STRING
): SpecificationsDto()

