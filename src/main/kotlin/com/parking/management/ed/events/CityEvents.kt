package com.parking.management.ed.events

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

data class CityCreatedEvent(
    val cityId: String,
)

data class CityUpdatedEvent(
    val cityId: String,
)

data class CityDeletedEvent(
    val cityId: String,
)

@Service
class CityEvents(
    val applicationEventPublisher: ApplicationEventPublisher
) {

    fun publishCityCreated(cityId: String) {
        applicationEventPublisher.publishEvent(CityCreatedEvent(cityId))
    }

    fun publishCityUpdated(cityId: String) {
        applicationEventPublisher.publishEvent(CityUpdatedEvent(cityId))
    }

    fun publishCityDeleted(cityId: String) {
        applicationEventPublisher.publishEvent(CityDeletedEvent(cityId))
    }
}

