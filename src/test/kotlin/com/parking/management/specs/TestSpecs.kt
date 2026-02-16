package com.parking.management.specs

import com.parking.management.features.parking.Parking
import com.parking.management.features.parking.ParkingRepository
import com.parking.management.specifications.*
import com.parking.management.test_objects.DatabaseReadyTestData
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import kotlin.test.Test


@DataJpaTest
class TestSpecs {
    @Autowired
    private val repository: ParkingRepository? = null

    @Autowired
    private val em: TestEntityManager? = null

    @BeforeEach
    fun setup() {
        // Step 1: Persist countries (no dependencies)
        val countriesSaved = DatabaseReadyTestData.countries().map { em!!.persist(it) }

        // Step 2: Persist cities with their country references
        val citiesSaved = countriesSaved.flatMap { country ->
            DatabaseReadyTestData.cities(country).map { em!!.persist(it) }
        }

        // Step 3: Persist parkings with their city references
        citiesSaved.forEach { city ->
            DatabaseReadyTestData.parkings(city).forEach { em!!.persist(it) }
        }
        em!!.flush()
    }

    @Test
    fun `should return all parkings`() {
        val allParkings = repository!!.findAll()
        println(allParkings.size)
        assert(allParkings.size == 20) { "Expected 20 parkings but got ${allParkings.size}" }
    }

    @Test
    fun `get parking with specs`() {
        val parking_ = repository!!.findAll(SpecsFactory.parkingByCapacity(20..70))
        print(parking_.size)
        println(parking_)
        assert(parking_.isEmpty()) { "Expected 0 parkings with capacity 20-70 but got ${parking_.size}" }
    }

    @Test
    fun `test RangeFilter on totalCapacity`() {
        val parking_ = repository!!.findAll(SpecsFactory.generalFilter<Parking>(
            filters = listOf(FilterObject(
                fieldName = "totalCapacity",
                filter = RangeFilter(200, 700)
            )),
        ))

        println(parking_.size)
        assert(parking_.size == 15) { "Expected 15 parkings with capacity 200-700 but got ${parking_.size}" }
    }

    @Test
    fun `test RangeFilter on currentOccupied`() {
        val parking_ = repository!!.findAll(SpecsFactory.generalFilter<Parking>(
            filters = listOf(FilterObject(
                fieldName = "currentOccupied",
                filter = RangeFilter(50, 200)
            )),
        ))

        assert(parking_.isNotEmpty()) { "Expected parkings with occupied 50-200" }
    }

    @Test
    fun `test RangeFilter with no end (greater than or equal)`() {
        val parking_ = repository!!.findAll(SpecsFactory.generalFilter<Parking>(
            filters = listOf(FilterObject(
                fieldName = "totalCapacity",
                filter = RangeFilter(start = 500, end = null)
            )),
        ))

        assert(parking_.all { it.totalCapacity >= 500 }) { "All parkings should have capacity >= 500" }
    }

    @Test
    fun `test StringFilter on name`() {
        val parking_ = repository!!.findAll(SpecsFactory.generalFilter<Parking>(
            filters = listOf(FilterObject(
                fieldName = "name",
                filter = StringFilter(value = "Downtown Parking")
            )),
        ))
        assert(parking_.all { it.name == "Downtown Parking" }) { "All parkings should have name 'Downtown Parking'" }
    }

    @Test
    fun `test StringListFilter on status`() {
        val parking_ = repository!!.findAll(SpecsFactory.generalFilter<Parking>(
            filters = listOf(FilterObject(
                fieldName = "status",
                filter = StringListFilter(listOfStrings = listOf("OPEN", "MAINTENANCE"))
            )),
        ))

        assert(parking_.isNotEmpty()) { "Should find parkings with OPEN or MAINTENANCE status" }
    }

    @Test
    fun `test DataRangeFilter on createdAt`() {
        val parking_ = repository!!.findAll(SpecsFactory.generalFilter<Parking>(
            filters = listOf(FilterObject(
                fieldName = "createdAt",
                filter = DateRangeFilter(
                    start = java.time.LocalDateTime.now().minusDays(1),
                    end = java.time.LocalDateTime.now().plusDays(1)
                )
            )),
        ))

        assert(parking_.isNotEmpty()) { "Should find parkings created within date range" }
    }

    @Test
    fun `test multiple filters combined`() {
        val parking_ = repository!!.findAll(SpecsFactory.generalFilter<Parking>(
            filters = listOf(
                FilterObject(fieldName = "totalCapacity", filter = RangeFilter(200, 700)),
                FilterObject(fieldName = "status", filter = StringListFilter(listOfStrings = listOf("OPEN")))
            ),
        ))

        assert(parking_.all { it.totalCapacity in 200..700 }) { "All should match capacity range" }
    }

    @Test
    fun `test join filter with StringFilter on city name`() {
        val parking_ = repository!!.findAll(SpecsFactory.generalFilterWithPathResolver<Parking>(
            filters = listOf(
                FilterObject(
                    fieldName = "city.name",
                    filter = StringFilter(value = "Paris")
                )
            )
        ))

        assert(parking_.all { it.city.name == "Paris" }) { "All parkings should be in Paris" }
    }

    @Test
    fun `test join filter with StringListFilter on city names`() {
        val parking_ = repository!!.findAll(SpecsFactory.generalFilterWithPathResolver<Parking>(
            filters = listOf(
                FilterObject(
                    fieldName = "city.name",
                    filter = StringListFilter(listOfStrings = listOf("Paris", "London"))
                )
            )
        ))
        println(parking_)
        assert(parking_.all { it.city.name in listOf("Paris", "London") }) { "All parkings should be in Paris or London" }
    }

    @Test
    fun `test join filter with RangeFilter on city zoom factor`() {
        val parking_ = repository!!.findAll(SpecsFactory.generalFilterWithPathResolver<Parking>(
            filters = listOf(
                FilterObject(
                    fieldName = "city.zoomFactor",
                    filter = RangeFilter(5, 15)
                )
            )
        ))

        assert(parking_.isNotEmpty()) { "Should find parkings in cities with zoom factor 5-15" }
    }

    @Test
    fun `test join filter with multiple join filters`() {
        val parking_ = repository!!.findAll(SpecsFactory.generalFilterWithPathResolver<Parking>(
            filters = listOf(
                FilterObject(fieldName = "city.name", filter = StringFilter(value = "Paris")),
                FilterObject(fieldName = "city.zoomFactor", filter = RangeFilter(5, 20))
            )
        ))

        assert(parking_.all { it.city.name == "Paris" && it.city.zoomFactor in 5..20 }) { "All should match both join conditions" }
    }

}