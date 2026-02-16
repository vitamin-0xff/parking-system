package com.parking.management.features.filter

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FilterRepository: MongoRepository<Filterable, String> {
    fun findAllByTableName(tableName: String): List<Filterable>
}
