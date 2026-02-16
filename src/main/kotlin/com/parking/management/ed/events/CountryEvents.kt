package com.parking.management.ed.events

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

data class CountryCreatedEvent(
    val countryId: String,
)

data class CountryUpdatedEvent(
    val countryId: String,
)

data class CountryDeletedEvent(
    val countryId: String,
)

@Service
class CountryEvents(
    val applicationEventPublisher: ApplicationEventPublisher
) {

    fun publishCountryCreated(countryId: String) {
        applicationEventPublisher.publishEvent(CountryCreatedEvent(countryId))
    }

    fun publishCountryUpdated(countryId: String) {
        applicationEventPublisher.publishEvent(CountryUpdatedEvent(countryId))
    }

    fun publishCountryDeleted(countryId: String) {
        applicationEventPublisher.publishEvent(CountryDeletedEvent(countryId))
    }
}

