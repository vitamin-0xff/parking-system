package com.parking.management.specifications

import com.parking.management.features.country.Country
import com.parking.management.features.parking.Parking
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime


//parkings {
//    cities
//    countries
//    total_capacity: range
//    current_occupied_capacity: range
//}

sealed class Filter

data class RangeFilter(
    val start: Int,
    val end: Int? = 0,
    val id: Int = 1,
): Filter()

data class StringFilter(
    val value: String,
    val id: Int = 2,
): Filter()

data class StringListFilter(
    val listOfStrings: List<String>,
    val id: Int = 3,
): Filter()

data class DataRangeFilter(
    val start: LocalDateTime,
    val end: LocalDateTime? = null,
    val id: Int = 5,
): Filter()



data class FilterObject(
    val fieldName: String,
    val filter: Filter
)

data class FilterJoin(
    val rootFieldName: String,
    val joinFieldName: String,
    val filter: Filter
)

object SpecsFactory {
    fun parkingByCities(cities: List<String>): Specification<Parking>? {
        if (cities.isEmpty()) return null
        return Specification { root, _, cb -> root.get<String>("city").`in`(cities) }
    }
    fun parkingByCountries(countries: List<String>): Specification<Parking>? {
        if (countries.isEmpty()) return null
        return Specification { root, _, cb -> root.get<String>("country").`in`(countries) }
    }

    fun findParkingByCountry(counties: List<String>): Specification<Parking> {

        /**
         * root is the entity to be selected from passible usage with in to return predicate
         * root.get("name")
         * root.get("age")
         * root.join("orders")
         *
         * queryCriteria: the whole query
         *  - sorting
         *  - grouping
         *  - distinct selection
         *  - projections
         *
         * queryBuilder: is like linkers and where clause,
         * where clause operators:
             * equal()
             * like()
             * greaterThan()
             * between()
             * and()
             * or()
             * isNull()
         * aggregation functions:
             * sum()
             * min()
             * max()
             * count()
             * avg()
         */
        return Specification { root, _, _ ->
            val countryPath = root.join<Parking, Country>("country", JoinType.INNER)
            countryPath.`in`(counties)
        }
    }
    fun parkingByCapacity(capacity: IntRange): Specification<Parking> {
        return Specification { root, _, cb -> cb.between(root.get<Int>("totalCapacity"), capacity.first, capacity.last) }
    }
    fun parkingByOccupiedCapacity(capacity: IntRange): Specification<Parking> {
        return Specification { root, _, cb -> cb.between(root.get<Int>("currentOccupiedCapacity"), capacity.first, capacity.last) }
    }

    inline fun <reified T: Any> generalFilter(filters: List<FilterObject>?): Specification<T> {
        if(filters == null) return Specification.where { from, builder ->  null }
        if (filters.isEmpty()) return Specification.where { from, builder ->  null }
        val predicateList = mutableListOf<Predicate>()
        return Specification {root, cq, cb ->
                // Iterates filters; adds predicates based on a filter type
                for (filter in filters) {
                    when(filter.filter) {
                        is RangeFilter -> {
                            if (filter.filter.end == null) predicateList.add(cb.greaterThanOrEqualTo(root.get(filter.fieldName), filter.filter.start))
                            else predicateList.add(cb.between(root.get(filter.fieldName), filter.filter.start, filter.filter.end))
                        }
                        is StringFilter -> predicateList.add(cb.equal(root.get<String>(filter.fieldName), filter.filter.value))
                        is StringListFilter -> predicateList.add(root.get<String>(filter.fieldName).`in`(filter.filter.listOfStrings))
                        is DataRangeFilter -> {
                            if (filter.filter.end == null) predicateList.add(cb.greaterThanOrEqualTo(root.get(filter.fieldName), filter.filter.start))
                            else predicateList.add(cb.between(root.get(filter.fieldName), filter.filter.start, filter.filter.end))
                        }
                    }
                }
            cb.and(*predicateList.toTypedArray())
            }
    }

    inline fun <reified T : Any, reified K : Any> generalFilterJoin(filters: List<FilterJoin>?): Specification<T> {
        if (filters.isNullOrEmpty()) return Specification { _, _, _ -> null }
        if (filters.isEmpty()) return Specification { _, _, _ -> null }
        val predicateList = mutableListOf<Predicate>()
        return Specification<T> {root, query, builder ->
            for (filter in filters) {
                val join = root.join<T, K>(filter.rootFieldName, JoinType.INNER)
                if (filter.filter is RangeFilter) {
                    if (filter.filter.end == null) {
                        predicateList.add(builder.greaterThanOrEqualTo(join.get(filter.joinFieldName), filter.filter.start))
                    } else {
                        predicateList.add(builder.between(join.get(filter.joinFieldName), filter.filter.start, filter.filter.end))
                    }
                }
                else if (filter.filter is StringFilter) predicateList.add(builder.equal(join.get<String>(filter.joinFieldName), filter.filter.value))
                else if (filter.filter is StringListFilter) predicateList.add(join.get<String>(filter.joinFieldName).`in`(filter.filter.listOfStrings))
                else if (filter.filter is DataRangeFilter) {
                    if (filter.filter.end == null) predicateList.add(builder.greaterThanOrEqualTo(join.get(filter.joinFieldName), filter.filter.start))
                    else predicateList.add(builder.between(join.get(filter.joinFieldName), filter.filter.start, filter.filter.end))
                }
            }
            builder.and(*predicateList.toTypedArray())
        }
    }
}

//object ParkingSpecsCount {
//    fun parkingCountByCitiesByCities(listOfCities: List<String>): Specification<Parking>? {
//        return Specification { root, query, cb ->
//        }
//    }
//}