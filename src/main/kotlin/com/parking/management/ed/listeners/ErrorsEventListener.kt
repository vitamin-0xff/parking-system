package com.parking.management.ed.listeners

import com.parking.management.ed.events.Error
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class ErrorsEventListener {

    /**
     * here we can do whatever we want!!
     * for example, we can log the error, send a notification, or even trigger some recovery process.
     */
    @EventListener
    fun handleErrorEvent(event: Error) {
        println("Error occurred: ${event.errorMessage}")
    }
}