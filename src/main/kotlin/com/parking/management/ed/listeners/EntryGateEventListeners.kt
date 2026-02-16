package com.parking.management.ed.listeners

import com.parking.management.ed.events.EntryGateCreatedEvent
import com.parking.management.ed.events.EntryGateDeletedEvent
import com.parking.management.ed.events.EntryGateUpdatedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class EntryGateEventListeners {

    @Async
    @EventListener
    fun handleEntryGateCreated(event: EntryGateCreatedEvent) {
        println("EntryGate created with id: ${event.entryGateId}")
    }

    @Async
    @EventListener
    fun handleEntryGateUpdated(event: EntryGateUpdatedEvent) {
        println("EntryGate updated with id: ${event.entryGateId}")
    }

    @Async
    @EventListener
    fun handleEntryGateDeleted(event: EntryGateDeletedEvent) {
        println("EntryGate deleted with id: ${event.entryGateId}")
    }
}

