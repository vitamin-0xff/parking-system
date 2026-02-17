package com.parking.management.specifications

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.parking.management.features.city.City
import com.parking.management.features.country.Country
import com.parking.management.features.entry_gate.EntryGate
import com.parking.management.features.entry_gate.EntryGateDirection
import com.parking.management.features.parking.Parking
import com.parking.management.features.parking.ParkingStatus
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.From
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime
import java.util.UUID


//parkings {
//    cities
//    countries
//    total_capacity: range
//    current_occupied_capacity: range
//}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = RangeFilter::class, name = "intRange"),
    JsonSubTypes.Type(value = DateRangeFilter::class, name = "dateRange"),
    JsonSubTypes.Type(value = StringFilter::class, name = "string"),
    JsonSubTypes.Type(value = StringListFilter::class, name = "stringList")
)
sealed class Filter

data class RangeFilter(
    val start: Int,
    val end: Int? = null,
): Filter()

data class StringFilter(
    val value: String,
): Filter()

data class StringListFilter(
    val listOfStrings: List<String>,
): Filter()

data class DateRangeFilter(
    val start: LocalDateTime,
    val end: LocalDateTime? = null,
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

data class Filters(
    val filters: List<FilterObject>,
)

object SpecsFactory {
    // Current Occupied Filters
    fun parkingByCurrentOccupiedRange(start: Int, end: Int?): Specification<Parking> {
        return Specification { root, _, cb ->
            if (end == null) {
                cb.greaterThanOrEqualTo(root.get("currentOccupied"), start)
            } else {
                cb.between(root.get("currentOccupied"), start, end)
            }
        }
    }

    // Current Occupied Filters
    fun parkingByStatus(statusEnum: List<String>): Specification<Parking> {
        if(statusEnum.isEmpty()) return Specification { _, _, _ -> null }
        return Specification { root, _, cb ->
            root.get<ParkingStatus>("status").`in`(statusEnum.map { ParkingStatus.valueOf(it) })
        }
    }

    // Total Capacity Filters
    fun parkingByTotalCapacityRange(start: Int, end: Int?): Specification<Parking> {
        return Specification { root, _, cb ->
            if (end == null) {
                cb.greaterThanOrEqualTo(root.get("totalCapacity"), start)
            } else {
                cb.between(root.get("totalCapacity"), start, end)
            }
        }
    }

    // City Name Filters
    fun parkingByCityName(cityName: String): Specification<Parking> {
        return Specification { root, _, cb ->
            val cityJoin = root.join<Parking, Any>("city", JoinType.INNER)
            cb.equal(cityJoin.get<String>("name"), cityName)
        }
    }

    fun parkingByCityNames(cityNames: List<String>): Specification<Parking>? {
        if (cityNames.isEmpty()) return null
        return Specification { root, _, _ ->
            val cityJoin = root.join<Parking, Any>("city", JoinType.INNER)
            cityJoin.get<String>("name").`in`(cityNames)
        }
    }

    // Country Name Filters (via City relationship: Parking -> City -> Country)
    fun parkingByCountryName(countryName: String): Specification<Parking> {
        return Specification { root, _, cb ->
            val cityJoin = root.join<Parking, Any>("city", JoinType.INNER)
            val countryJoin = cityJoin.join<Any, Country>("country", JoinType.INNER)
            cb.equal(countryJoin.get<String>("name"), countryName)
        }
    }

    fun parkingByCountryNames(countryNames: List<String>): Specification<Parking>? {
        if (countryNames.isEmpty()) return null
        return Specification { root, _, _ ->
            val cityJoin = root.join<Parking, Any>("city", JoinType.INNER)
            val countryJoin = cityJoin.join<Any, Country>("country", JoinType.INNER)
            countryJoin.get<String>("name").`in`(countryNames)
        }
    }

    fun parkingByCities(cities: List<String>): Specification<Parking>? {
        if (cities.isEmpty()) return null
        return Specification { root, _, _ ->
            val cityJoin = root.join<Parking, Any>("city", JoinType.INNER)
            cityJoin.get<String>("name").`in`(cities)
        }
    }

    fun parkingByCountries(countries: List<String>): Specification<Parking>? {
        if (countries.isEmpty()) return null
        return Specification { root, _, _ ->
            val cityJoin = root.join<Parking, Any>("city", JoinType.INNER)
            val countryJoin = cityJoin.join<Any, Country>("country", JoinType.INNER)
            countryJoin.get<String>("name").`in`(countries)
        }
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
            val cityJoin = root.join<Parking, Any>("city", JoinType.INNER)
            val countryJoin = cityJoin.join<Any, Country>("country", JoinType.INNER)
            countryJoin.get<String>("name").`in`(counties)
        }
    }

    fun parkingByCapacity(capacity: IntRange): Specification<Parking> {
        return Specification { root, _, cb ->
            cb.between(
                root.get<Int>("totalCapacity"),
                capacity.first,
                capacity.last
            )
        }
    }
    fun parkingByCapacity(start: Int, end: Int? = null): Specification<Parking> {
        return Specification { root, _, cb ->

            if(end == null) {
                cb.greaterThanOrEqualTo(
                    root.get<Int>("totalCapacity"),
                    start
                )
            } else
            cb.between(
                root.get<Int>("totalCapacity"),
                start,
                end
            )
        }
    }

    fun parkingByOccupiedCapacity(capacity: IntRange): Specification<Parking> {
        return Specification { root, _, cb ->
            cb.between(
                root.get<Int>("currentOccupied"),
                capacity.first,
                capacity.last
            )
        }
    }

    fun parkingByCreatedAtRange(start: LocalDateTime, end: LocalDateTime?): Specification<Parking> {
        return Specification { root, _, cb ->
            if (end == null) {
                cb.greaterThanOrEqualTo(root.get("createdAt"), start)
            } else {
                cb.between(root.get("createdAt"), start, end)
            }
        }
    }

    // City filters
    fun cityByNames(names: List<String>): Specification<City>? {
        if (names.isEmpty()) return null
        return Specification { root, _, _ -> root.get<String>("name").`in`(names) }
    }

    fun cityByStateCodes(stateCodes: List<String>): Specification<City>? {
        if (stateCodes.isEmpty()) return null
        return Specification { root, _, _ -> root.get<String>("stateCode").`in`(stateCodes) }
    }

    fun cityByCountryNames(countryNames: List<String>): Specification<City>? {
        if (countryNames.isEmpty()) return null
        return Specification { root, _, _ ->
            val countryJoin = root.join<City, Country>("country", JoinType.INNER)
            countryJoin.get<String>("name").`in`(countryNames)
        }
    }

    fun cityByZoomFactorRange(start: Int, end: Int?): Specification<City> {
        return Specification { root, _, cb ->
            if (end == null) cb.greaterThanOrEqualTo(root.get("zoomFactor"), start)
            else cb.between(root.get("zoomFactor"), start, end)
        }
    }

    fun cityByCreatedAtRange(start: LocalDateTime, end: LocalDateTime?): Specification<City> {
        return Specification { root, _, cb ->
            if (end == null) cb.greaterThanOrEqualTo(root.get("createdAt"), start)
            else cb.between(root.get("createdAt"), start, end)
        }
    }

    // Country filters
    fun countryByNames(names: List<String>): Specification<Country>? {
        if (names.isEmpty()) return null
        return Specification { root, _, _ -> root.get<String>("name").`in`(names) }
    }

    fun countryByIsoCodes(isoCodes: List<String>): Specification<Country>? {
        if (isoCodes.isEmpty()) return null
        return Specification { root, _, _ -> root.get<String>("isoCode").`in`(isoCodes) }
    }

    fun countryByZoomFactorRange(start: Int, end: Int?): Specification<Country> {
        return Specification { root, _, cb ->
            if (end == null) cb.greaterThanOrEqualTo(root.get("zoomFactor"), start)
            else cb.between(root.get("zoomFactor"), start, end)
        }
    }

    fun countryByCreatedAtRange(start: LocalDateTime, end: LocalDateTime?): Specification<Country> {
        return Specification { root, _, cb ->
            if (end == null) cb.greaterThanOrEqualTo(root.get("createdAt"), start)
            else cb.between(root.get("createdAt"), start, end)
        }
    }

    // Entry gate filters
    fun entryGateByNames(names: List<String>): Specification<EntryGate>? {
        if (names.isEmpty()) return null
        return Specification { root, _, _ -> root.get<String>("name").`in`(names) }
    }

    fun entryGateByDirections(directions: List<EntryGateDirection>): Specification<EntryGate>? {
        if (directions.isEmpty()) return null
        return Specification { root, _, _ -> root.get<EntryGateDirection>("direction").`in`(directions) }
    }

    fun entryGateByHardwareIds(hardwareIds: List<String>): Specification<EntryGate>? {
        if (hardwareIds.isEmpty()) return null
        return Specification { root, _, _ -> root.get<String>("hardwareId").`in`(hardwareIds) }
    }

    fun entryGateByIsActive(isActive: Boolean): Specification<EntryGate> {
        return Specification { root, _, cb -> cb.equal(root.get<Boolean>("isActive"), isActive) }
    }

    fun entryGateByParkingIds(parkingIds: List<UUID>): Specification<EntryGate>? {
        if (parkingIds.isEmpty()) return null
        return Specification { root, _, _ ->
            val parkingJoin = root.join<EntryGate, Any>("parking", JoinType.INNER)
            parkingJoin.get<UUID>("id").`in`(parkingIds)
        }
    }

    fun entryGateByCreatedAtRange(start: LocalDateTime, end: LocalDateTime?): Specification<EntryGate> {
        return Specification { root, _, cb ->
            if (end == null) cb.greaterThanOrEqualTo(root.get("createdAt"), start)
            else cb.between(root.get("createdAt"), start, end)
        }
    }

    /**
     * one single got all types of filter, no need for other similar methods or data types
     */
    inline fun <reified T : Any> generalFilter(filters: List<FilterObject>?): Specification<T> {
        if (filters == null) return Specification.where { _, _ -> null }
        if (filters.isEmpty()) return Specification.where { _, _ -> null }
        val predicateList = mutableListOf<Predicate>()
        return Specification { root, _, cb ->
            // Iterates filters; adds predicates based on a filter type
            for (filter in filters) {
                if (filter.fieldName.contains(".")) {
                    val (rootName, fieldName) = filter.fieldName.split(".")
                    val joinPath = root.join<T, Any>(rootName, JoinType.INNER)
                    when (filter.filter) {
                        is RangeFilter -> {
                            if (filter.filter.end == null) predicateList.add(
                                cb.greaterThanOrEqualTo(
                                    joinPath.get(
                                        fieldName
                                    ), filter.filter.start
                                )
                            )
                            else predicateList.add(
                                cb.between(
                                    joinPath.get(fieldName),
                                    filter.filter.start,
                                    filter.filter.end
                                )
                            )
                        }

                        is StringFilter -> predicateList.add(
                            cb.equal(
                                joinPath.get<String>(fieldName),
                                filter.filter.value
                            )
                        )

                        is StringListFilter -> predicateList.add(
                            joinPath.get<String>(fieldName).`in`(filter.filter.listOfStrings)
                        )

                        is DateRangeFilter -> {
                            if (filter.filter.end == null) predicateList.add(
                                cb.greaterThanOrEqualTo(
                                    joinPath.get(
                                        fieldName
                                    ), filter.filter.start
                                )
                            )
                            else predicateList.add(
                                cb.between(
                                    joinPath.get(fieldName),
                                    filter.filter.start,
                                    filter.filter.end
                                )
                            )
                        }
                    }
                } else {
                    when (filter.filter) {
                        is RangeFilter -> {
                            if (filter.filter.end == null) predicateList.add(
                                cb.greaterThanOrEqualTo(
                                    root.get(filter.fieldName),
                                    filter.filter.start
                                )
                            )
                            else predicateList.add(
                                cb.between(
                                    root.get(filter.fieldName),
                                    filter.filter.start,
                                    filter.filter.end
                                )
                            )
                        }

                        is StringFilter -> predicateList.add(
                            cb.equal(
                                root.get<String>(filter.fieldName),
                                filter.filter.value
                            )
                        )

                        is StringListFilter -> predicateList.add(
                            root.get<String>(filter.fieldName).`in`(filter.filter.listOfStrings)
                        )

                        is DateRangeFilter -> {
                            if (filter.filter.end == null) predicateList.add(
                                cb.greaterThanOrEqualTo(
                                    root.get(filter.fieldName),
                                    filter.filter.start
                                )
                            )
                            else predicateList.add(
                                cb.between(
                                    root.get(filter.fieldName),
                                    filter.filter.start,
                                    filter.filter.end
                                )
                            )
                        }
                    }
                }
            }
            cb.and(*predicateList.toTypedArray())
        }
    }

    fun resolvePath(root: From<*, *>, path: String): Path<*> {
        val parts = path.split(".")

        if (parts.size == 1) {
            // Simple field access
            return root.get<Any>(path)
        }

        // Navigate through joins
        var current: Path<*> = root
        for (i in 0 until parts.size - 1) {
            current = (current as From<*, *>).join<Any, Any>(parts[i], JoinType.INNER)
        }

        return (current as From<*, *>).get<Any>(parts.last())
    }


    /**
     * one single got all types of filter, no need for other similar methods or data types
     */
    inline fun <reified T : Any> generalFilterWithPathResolver(filters: List<FilterObject>?): Specification<T> {
        if (filters.isNullOrEmpty()) return Specification { _, _, _ -> null }

        return Specification { root, _, cb ->
            val predicateList = mutableListOf<Predicate>()

            for (filter in filters) {
                val path = resolvePath(root, filter.fieldName)

                when (filter.filter) {
                    is RangeFilter -> {
                        if (filter.filter.end == null) {
                            predicateList.add(cb.greaterThanOrEqualTo(path as Expression<Int>, filter.filter.start))
                        } else {
                            predicateList.add(cb.between(path as Expression<Int>, filter.filter.start, filter.filter.end))
                        }
                    }

                    is StringFilter -> predicateList.add(cb.equal(path, filter.filter.value))
                    is StringListFilter -> predicateList.add((path).`in`(filter.filter.listOfStrings))
                    is DateRangeFilter -> {
                        if (filter.filter.end == null) {
                            predicateList.add(cb.greaterThanOrEqualTo(path as Expression<LocalDateTime>, filter.filter.start))
                        } else {
                            predicateList.add(cb.between(path as Expression<LocalDateTime>, filter.filter.start, filter.filter.end))
                        }
                    }
                }
            }
            cb.and(*predicateList.toTypedArray())
        }
    }
}

//object ParkingSpecsCount {
//    fun parkingCountByCitiesByCities(listOfCities: List<String>): Specification<Parking>? {
//        return Specification { root, query, cb ->
//        }
//    }
//}