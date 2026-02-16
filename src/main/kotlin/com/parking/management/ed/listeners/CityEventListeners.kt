package com.parking.management.ed.listeners

import com.parking.management.ed.events.CityCreatedEvent
import com.parking.management.ed.events.CityDeletedEvent
import com.parking.management.ed.events.CityUpdatedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component

class CityEventListeners {

    @Async
    @EventListener
    fun handleCityCreated(event: CityCreatedEvent) {
        println("City created with id: ${event.cityId}")
    }

    @Async
    @EventListener
    fun handleCityUpdated(event: CityUpdatedEvent) {
        println("City updated with id: ${event.cityId}")
    }

    @Async
    @EventListener
    fun handleCityDeleted(event: CityDeletedEvent) {
        println("City deleted with id: ${event.cityId}")
    }
}

