package com.parking.management.ed.listeners

import com.parking.management.ed.events.CountryCreatedEvent
import com.parking.management.ed.events.CountryDeletedEvent
import com.parking.management.ed.events.CountryUpdatedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class CountryEventListeners {

    @Async
    @EventListener
    fun handleCountryCreated(event: CountryCreatedEvent) {
        println("Country created with id: ${event.countryId}")
    }

    @Async
    @EventListener
    fun handleCountryUpdated(event: CountryUpdatedEvent) {
        println("Country updated with id: ${event.countryId}")
    }

    @Async
    @EventListener
    fun handleCountryDeleted(event: CountryDeletedEvent) {
        println("Country deleted with id: ${event.countryId}")
    }
}

