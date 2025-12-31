package com.parking.management.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate


@Configuration
class MongoConfig {
    @Value($$"${spring.data.mongodb.uri}")
    private val mongoUri: String? = null

    @Bean
    fun mongoClient(): MongoClient {
        val connectionString = ConnectionString(mongoUri!!)
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()
        return MongoClients.create(mongoClientSettings)
    }

    @Bean
    fun mongoTemplate(mongoClient: MongoClient): MongoTemplate =
        MongoTemplate(mongoClient, "mongodb-parking-db")

    private fun connectionStringDatabase(): String? {
        val connectionString = ConnectionString(mongoUri!!)
        return connectionString.database // extracts database from URI
    }
}