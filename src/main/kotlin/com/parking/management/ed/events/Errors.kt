package com.parking.management.ed.events

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

data class Error(
    val errorMessage: String,
    val code: Int? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Component
class ErrorsEventDispatcher(
    val applicationEventPublisher: ApplicationEventPublisher

) {
    fun pushErrorEvent(
        error: Exception
    ) {
        when (error) {
            is IllegalArgumentException -> applicationEventPublisher.publishEvent(
                Error(
                    errorMessage = error.message ?: "Illegal argument",
                    code = 400
                )
            )
            is IllegalStateException -> applicationEventPublisher.publishEvent(
                Error(
                    errorMessage = error.message ?: "Illegal state",
                    code = 409
                )
            )

            is NullPointerException -> applicationEventPublisher.publishEvent(
                Error(
                    errorMessage = error.message ?: "Null pointer exception",
                    code = 500
                )
            )
            is IndexOutOfBoundsException  -> applicationEventPublisher.publishEvent(
                Error(
                    errorMessage = error.message ?: "Index out of bounds",
                    code = 400
                )
            )
            else ->
            applicationEventPublisher.publishEvent(Error(
                errorMessage = error.message ?: "Unknown error",
                code = null
        ))
    }
}
}