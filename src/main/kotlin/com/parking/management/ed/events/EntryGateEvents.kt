package com.parking.management.ed.events

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

data class EntryGateCreatedEvent(
    val entryGateId: String,
)

data class EntryGateUpdatedEvent(
    val entryGateId: String,
)

data class EntryGateDeletedEvent(
    val entryGateId: String,
)

@Service
class EntryGateEvents(
    val applicationEventPublisher: ApplicationEventPublisher
) {

    fun publishEntryGateCreated(entryGateId: String) {
        applicationEventPublisher.publishEvent(EntryGateCreatedEvent(entryGateId))
    }

    fun publishEntryGateUpdated(entryGateId: String) {
        applicationEventPublisher.publishEvent(EntryGateUpdatedEvent(entryGateId))
    }

    fun publishEntryGateDeleted(entryGateId: String) {
        applicationEventPublisher.publishEvent(EntryGateDeletedEvent(entryGateId))
    }
}

