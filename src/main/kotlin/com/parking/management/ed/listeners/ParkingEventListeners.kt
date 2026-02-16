package com.parking.management.ed.listeners

import com.parking.management.ed.events.ParkingCreatedEvent
import com.parking.management.ed.events.ParkingDeletedEvent
import com.parking.management.ed.events.ParkingUpdatedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class ParkingEventListeners {

    @Async
    @EventListener
    fun handleParkingCreated(event: ParkingCreatedEvent) {
        println("Parking created with id: ${event.parkingId}")
    }

    @Async
    @EventListener
    fun handleParkingUpdated(event: ParkingUpdatedEvent) {
        println("Parking updated with id: ${event.parkingId}")
    }

    @Async
    @EventListener
    fun handleParkingDeleted(event: ParkingDeletedEvent) {
        println("Parking updated with id: ${event.parkingId}")
    }
}