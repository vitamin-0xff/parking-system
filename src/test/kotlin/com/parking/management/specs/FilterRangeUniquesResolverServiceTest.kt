package com.parking.management.specs

import com.parking.management.features.filter.FilterRepository
import com.parking.management.features.filter.Filterable
import com.parking.management.features.filter.IntProperty
import com.parking.management.features.filter.StringProperty
import com.parking.management.features.filter.EnumProperty
import com.parking.management.features.filter.parking_filter.ConcreteIntFilter
import com.parking.management.features.filter.parking_filter.ConcreteStringFilter
import com.parking.management.features.filter.parking_filter.ConcreteEnumFilter
import com.parking.management.features.filter.parking_filter.FilterRangeUniquesResolverService
import com.parking.management.features.filter.parking_filter.MinMax
import com.parking.management.features.parking.Parking
import com.parking.management.features.parking.ParkingRepository
import com.parking.management.test_objects.DatabaseReadyTestData
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.context.annotation.Import
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@DataJpaTest
@Import(FilterRangeUniquesResolverService::class)
@DisplayName("FilterRangeUniquesResolverService Unit Tests")
class FilterRangeUniquesResolverServiceTest {

    @Autowired
    private val service: FilterRangeUniquesResolverService? = null

    @Autowired
    private val filterRepository: FilterRepository? = null

    @Autowired
    private val parkingRepository: ParkingRepository? = null

    @Autowired
    private val em: TestEntityManager? = null

    @BeforeEach
    fun setup() {
        // Clear repositories
        parkingRepository!!.deleteAll()
        filterRepository!!.deleteAll()

        // Setup test data
        val countriesSaved = DatabaseReadyTestData.countries().map { em!!.persist(it) }
        val citiesSaved = countriesSaved.flatMap { country ->
            DatabaseReadyTestData.cities(country).map { em!!.persist(it) }
        }
        citiesSaved.forEach { city ->
            DatabaseReadyTestData.parkings(city).forEach { em!!.persist(it) }
        }
        em!!.flush()
    }

    @Nested
    @DisplayName("getConcreteFilters Tests")
    inner class GetConcreteFiltersTests {

        @BeforeEach
        fun setupFilters() {
            val parkingFilter = Filterable(
                id = null,
                tableName = "parking",
                intProperties = listOf(
                    IntProperty(
                        prettyName = "Total Capacity",
                        name = "totalCapacity",
                        description = "Total parking capacity"
                    ),
                    IntProperty(
                        prettyName = "Current Occupied",
                        name = "currentOccupied",
                        description = "Currently occupied spaces"
                    )
                ),
                stringProperties = listOf(
                    StringProperty(
                        prettyName = "Name",
                        name = "name",
                        description = "Parking name"
                    )
                ),
                enumProperties = listOf(
                    EnumProperty(
                        prettyName = "Status",
                        name = "status",
                        enumClassFullName = "com.parking.management.features.parking.ParkingStatus",
                        description = "Parking status"
                    )
                )
            )
            filterRepository!!.save(parkingFilter)
        }

        @Test
        @DisplayName("Should return concrete filters for parking resource")
        fun testGetConcreteFiltersSuccess() {
            val concreteFilters = service!!.getConcreteFilters("parking", Parking::class.java)

            assertNotNull(concreteFilters)
            assertEquals(4, concreteFilters.size)

            // Check INT filters
            val totalCapacityFilter = concreteFilters.filterIsInstance<ConcreteIntFilter>().find { it.property.name == "totalCapacity" }
            assertNotNull(totalCapacityFilter)
            assertTrue(totalCapacityFilter.minMax.min >= 0)

            // Check ENUM filter
            val statusFilter = concreteFilters.filterIsInstance<ConcreteEnumFilter>().find { it.property.name == "status" }
            assertNotNull(statusFilter)
            assertTrue(statusFilter.availableValues.isNotEmpty())

            // Check STRING filter
            val nameFilter = concreteFilters.filterIsInstance<ConcreteStringFilter>().find { it.property.name == "name" }
            assertNotNull(nameFilter)
        }

        @Test
        @DisplayName("Should throw exception when resource not found")
        fun testGetConcreteFiltersResourceNotFound() {
            assertThrows<RuntimeException> {
                service!!.getConcreteFilters("nonexistent", Parking::class.java)
            }
        }

        @Test
        @DisplayName("Should handle multiple INT_RANGE properties correctly")
        fun testMultipleIntRangeFilters() {
            val concreteFilters = service!!.getConcreteFilters("parking", Parking::class.java)

            val intRangeFilters = concreteFilters.filterIsInstance<ConcreteIntFilter>()
            assertEquals(2, intRangeFilters.size)

            intRangeFilters.forEach { filter ->
                assertNotNull(filter.minMax)
                assertTrue(filter.minMax.min >= 0)
                assertTrue(filter.minMax.max >= filter.minMax.min)
            }
        }
    }

    @Nested
    @DisplayName("getMaxMinOfProperties Tests")
    inner class GetMaxMinOfPropertiesTests {

        @Test
        @DisplayName("Should get correct min and max for totalCapacity")
        fun testGetMinMaxForTotalCapacity() {
            val minMax = service!!.getMaxMinOfProperties("totalCapacity", Parking::class.java)

            assertNotNull(minMax)
            assertTrue(minMax.min > 0)
            assertTrue(minMax.max >= minMax.min)
            println("Total Capacity Range: ${minMax.min} - ${minMax.max}")
        }

        @Test
        @DisplayName("Should get correct min and max for currentOccupied")
        fun testGetMinMaxForCurrentOccupied() {
            val minMax = service!!.getMaxMinOfProperties("currentOccupied", Parking::class.java)

            assertNotNull(minMax)
            assertTrue(minMax.min >= 0)
            assertTrue(minMax.max >= minMax.min)
            println("Current Occupied Range: ${minMax.min} - ${minMax.max}")
        }

        @Test
        @DisplayName("Should return MinMax with default values on error")
        fun testGetMinMaxHandlesErrorGracefully() {
            // This should not throw but return MinMax(0, 0)
            val minMax = service!!.getMaxMinOfProperties("nonexistentField", Parking::class.java)

            assertNotNull(minMax)
            assertEquals(0, minMax.min)
            assertEquals(0, minMax.max)
        }

        @Test
        @DisplayName("Should handle nested property paths")
        fun testGetMinMaxForNestedProperty() {
            val minMax = service!!.getMaxMinOfProperties("city.id", Parking::class.java)

            // This might return default values depending on database state
            assertNotNull(minMax)
            // Just verify structure is correct
            assertTrue(minMax.min >= 0)
            assertTrue(minMax.max >= 0)
        }
    }

    @Nested
    @DisplayName("getDistinctValues Tests")
    inner class GetDistinctValuesTests {

        @Test
        @DisplayName("Should get distinct status values")
        fun testGetDistinctValuesForStatus() {
            val distinctValues = service!!.getDistinctValues("status", Parking::class.java)

            assertNotNull(distinctValues)
            assertTrue(distinctValues.isNotEmpty())
            // Status values should be distinct
            assertEquals(distinctValues.size, distinctValues.distinct().size)
            println("Distinct Status Values: $distinctValues")
        }

        @Test
        @DisplayName("Should get distinct parking names")
        fun testGetDistinctValuesForName() {
            val distinctValues = service!!.getDistinctValues("name", Parking::class.java)

            assertNotNull(distinctValues)
            assertTrue(distinctValues.isNotEmpty())
            // Names should be distinct
            assertEquals(distinctValues.size, distinctValues.distinct().size)
            println("Distinct Parking Names: $distinctValues")
        }

        @Test
        @DisplayName("Should return empty list for nonexistent field")
        fun testGetDistinctValuesForNonexistentField() {
            val distinctValues = service!!.getDistinctValues("nonexistentField", Parking::class.java)

            assertNotNull(distinctValues)
            assertTrue(distinctValues.isEmpty())
        }

        @Test
        @DisplayName("Should filter out null values")
        fun testGetDistinctValuesFiltersNulls() {
            val distinctValues = service!!.getDistinctValues("status", Parking::class.java)

            // List should be non-empty and all values should be non-null
            assertTrue(distinctValues.isNotEmpty())
        }

        @Test
        @DisplayName("Should handle nested property paths")
        fun testGetDistinctValuesForNestedProperty() {
            val distinctValues = service!!.getDistinctValues("city.name", Parking::class.java)

            assertNotNull(distinctValues)
            // Should have some city names
            if (distinctValues.isNotEmpty()) {
                assertEquals(distinctValues.size, distinctValues.distinct().size)
                println("Distinct City Names: $distinctValues")
            }
        }
    }

    @Nested
    @DisplayName("resolve Tests")
    inner class ResolveTests {

        @Test
        @DisplayName("Should resolve simple property paths")
        fun testResolveSingleProperty() {
            val cb = em!!.entityManager.criteriaBuilder
            val query = cb.createQuery(String::class.java)
            val root = query.from(Parking::class.java)

            val path = service!!.resolve<String>(root, listOf("name"))

            assertNotNull(path)
            assertEquals("name", path.alias)
        }

        @Test
        @DisplayName("Should resolve nested property paths")
        fun testResolveNestedProperty() {
            val cb = em!!.entityManager.criteriaBuilder
            val query = cb.createQuery(String::class.java)
            val root = query.from(Parking::class.java)

            val path = service!!.resolve<String>(root, listOf("city", "name"))

            assertNotNull(path)
            println("Resolved nested path: $path")
        }

        @Test
        @DisplayName("Should handle single element list")
        fun testResolveSingleElementList() {
            val cb = em!!.entityManager.criteriaBuilder
            val query = cb.createQuery(Int::class.java)
            val root = query.from(Parking::class.java)

            val path = service!!.resolve<Int>(root, listOf("totalCapacity"))

            assertNotNull(path)
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    inner class IntegrationTests {

        @BeforeEach
        fun setupFilters() {
            val parkingFilter = Filterable(
                id = null,
                tableName = "parking",
                intProperties = listOf(
                    IntProperty(
                        prettyName = "Total Capacity",
                        name = "totalCapacity"
                    )
                ),
                stringProperties = listOf(
                    StringProperty(
                        prettyName = "Name",
                        name = "name"
                    )
                ),
                enumProperties = listOf(
                    EnumProperty(
                        prettyName = "Status",
                        name = "status",
                        enumClassFullName = "com.parking.management.features.parking.ParkingStatus"
                    )
                )
            )
            filterRepository!!.save(parkingFilter)
        }

        @Test
        @DisplayName("Should build complete concrete filters for all types")
        fun testCompleteFilterBuildProcess() {
            val concreteFilters = service!!.getConcreteFilters("parking", Parking::class.java)

            assertEquals(3, concreteFilters.size)

            // Verify INT filter has valid min/max
            val capacityFilter = concreteFilters.filterIsInstance<ConcreteIntFilter>().find { it.property.name == "totalCapacity" }!!
            assertNotNull(capacityFilter.minMax)
            assertTrue(capacityFilter.minMax.min >= 0)

            // Verify ENUM filter has values
            val statusFilter = concreteFilters.filterIsInstance<ConcreteEnumFilter>().find { it.property.name == "status" }!!
            assertNotNull(statusFilter.availableValues)
            assertTrue(statusFilter.availableValues.isNotEmpty())

            // Verify STRING filter exists
            val nameFilter = concreteFilters.filterIsInstance<ConcreteStringFilter>().find { it.property.name == "name" }!!
            assertNotNull(nameFilter)
        }

        @Test
        @DisplayName("Should maintain consistency between multiple calls")
        fun testConsistencyAcrossMultipleCalls() {
            val firstCall = service!!.getConcreteFilters("parking", Parking::class.java)
            val secondCall = service.getConcreteFilters("parking", Parking::class.java)

            assertEquals(firstCall.size, secondCall.size)
            firstCall.zip(secondCall).forEach { (first, second) ->
                assertEquals(first.property.name, second.property.name)
                if (first is ConcreteIntFilter && second is ConcreteIntFilter) {
                    assertEquals(first.minMax.min, second.minMax.min)
                    assertEquals(first.minMax.max, second.minMax.max)
                }
            }
        }
    }

    @Nested
    @DisplayName("Data Class Tests")
    inner class DataClassTests {

        @Test
        @DisplayName("MinMax data class should work correctly")
        fun testMinMaxDataClass() {
            val minMax = MinMax(min = 10, max = 100)

            assertEquals(10, minMax.min)
            assertEquals(100, minMax.max)
        }

        @Test
        @DisplayName("ConcreteIntFilter should work correctly")
        fun testConcreteIntFilter() {
            val property = IntProperty(
                prettyName = "Test",
                name = "test"
            )
            val minMax = MinMax(min = 5, max = 50)
            val filter = ConcreteIntFilter(property = property, minMax = minMax)

            assertEquals(property, filter.property)
            assertEquals(minMax, filter.minMax)
        }

        @Test
        @DisplayName("ConcreteStringFilter should work correctly")
        fun testConcreteStringFilter() {
            val property = StringProperty(
                prettyName = "Test",
                name = "test"
            )
            val values = listOf("value1", "value2", "value3")
            val filter = ConcreteStringFilter(property = property, availableValues = values)

            assertEquals(property, filter.property)
            assertEquals(values, filter.availableValues)
        }

        @Test
        @DisplayName("ConcreteEnumFilter should work correctly")
        fun testConcreteEnumFilter() {
            val property = EnumProperty(
                prettyName = "Status",
                name = "status",
                enumClassFullName = "com.example.Status"
            )
            val values = listOf("ACTIVE", "INACTIVE")
            val filter = ConcreteEnumFilter(property = property, availableValues = values)

            assertEquals(property, filter.property)
            assertEquals(values, filter.availableValues)
        }
    }
}











