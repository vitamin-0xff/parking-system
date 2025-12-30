package com.parking.management.testcontrollers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/")
class RootController {
    @GetMapping
    fun root(): Map<String, String> {
        return mapOf("message" to "Hello, world!")
    }
}