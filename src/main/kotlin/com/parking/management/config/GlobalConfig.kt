package com.parking.management.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GlobalConfig {
        @Bean
        fun objectMapper(): ObjectMapper {
            return ObjectMapper()
        }
}