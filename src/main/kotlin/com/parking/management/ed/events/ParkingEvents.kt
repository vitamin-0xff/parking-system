package com.parking.management.ed.events

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

data class ParkingCreatedEvent(
    val parkingId: String,
)

data class ParkingUpdatedEvent(
    val parkingId: String,
)

data class ParkingDeletedEvent(
    val parkingId: String,
)

@Service
class ParkingEvents(
    val applicationEventPublisher: ApplicationEventPublisher
) {

    fun publishParkingCreated(parkingId: String) {
        applicationEventPublisher.publishEvent(ParkingCreatedEvent(parkingId))
    }

    fun publishParkingUpdated(parkingId: String) {
        applicationEventPublisher.publishEvent(ParkingUpdatedEvent(parkingId))
    }

    fun publishParkingDeleted(parkingId: String) {
        applicationEventPublisher.publishEvent(ParkingDeletedEvent(parkingId))
    }
}