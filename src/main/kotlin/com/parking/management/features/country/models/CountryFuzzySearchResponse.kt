package com.parking.management.features.country.models

import java.util.UUID

interface CountryFuzzySearchResponse {
    val id: UUID
    val name: String
    val isoCode: String
    val latitude: Double
    val longitude: Double
    val zoomFactor: String
    val sim: Float
}