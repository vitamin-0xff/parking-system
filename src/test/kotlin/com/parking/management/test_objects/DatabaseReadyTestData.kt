package com.parking.management.test_objects

import com.parking.management.features.city.City
import com.parking.management.features.country.Country
import com.parking.management.features.parking.Parking
import com.parking.management.features.parking.ParkingStatus

/**
 * Database-ready test data structure optimized for entity persistence.
 *
 * Usage example:
 * ```
 * val countriesSaved = DatabaseReadyTestData.countries().map { em.persist(it) }
 * val citiesSaved = countriesSaved.flatMap { country ->
 *     DatabaseReadyTestData.cities(country).map { em.persist(it) }
 * }
 * val parkingsSaved = citiesSaved.flatMap { city ->
 *     DatabaseReadyTestData.parkings(city).map { em.persist(it) }
 * }
 * em.flush()
 * ```
 */
object DatabaseReadyTestData {

    // Step 1: Persist countries first (no dependencies)
    fun countries() = listOf(
        Country(
            name = "United States",
            isoCode = "US",
            latitude = 37.0902,
            longitude = -95.7129,
            zoomFactor = 4
        ),
        Country(
            name = "France",
            isoCode = "FR",
            latitude = 46.2276,
            longitude = 2.2137,
            zoomFactor = 6
        ),
        Country(
            name = "Germany",
            isoCode = "DE",
            latitude = 51.1657,
            longitude = 10.4515,
            zoomFactor = 6
        ),
        Country(
            name = "Japan",
            isoCode = "JP",
            latitude = 36.2048,
            longitude = 138.2529,
            zoomFactor = 5
        ),
        Country(
            name = "United Kingdom",
            isoCode = "GB",
            latitude = 55.3781,
            longitude = -3.4360,
            zoomFactor = 6
        )
    )

    // Step 2: Persist cities (requires persisted country)
    fun cities(country: Country) = when(country.isoCode) {
        "US" -> listOf(
            City(
                name = "New York",
                stateCode = "NY",
                latitude = 40.7128,
                longitude = -74.0060,
                zoomFactor = 12,
                country = country
            ),
            City(
                name = "Los Angeles",
                stateCode = "CA",
                latitude = 34.0522,
                longitude = -118.2437,
                zoomFactor = 11,
                country = country
            )
        )
        "FR" -> listOf(
            City(
                name = "Paris",
                stateCode = "IDF",
                latitude = 48.8566,
                longitude = 2.3522,
                zoomFactor = 12,
                country = country
            ),
            City(
                name = "Lyon",
                stateCode = "ARA",
                latitude = 45.7640,
                longitude = 4.8357,
                zoomFactor = 12,
                country = country
            )
        )
        "DE" -> listOf(
            City(
                name = "Berlin",
                stateCode = "BE",
                latitude = 52.5200,
                longitude = 13.4050,
                zoomFactor = 11,
                country = country
            ),
            City(
                name = "Munich",
                stateCode = "BY",
                latitude = 48.1351,
                longitude = 11.5820,
                zoomFactor = 11,
                country = country
            )
        )
        "JP" -> listOf(
            City(
                name = "Tokyo",
                stateCode = "13",
                latitude = 35.6762,
                longitude = 139.6503,
                zoomFactor = 11,
                country = country
            ),
            City(
                name = "Osaka",
                stateCode = "27",
                latitude = 34.6937,
                longitude = 135.5023,
                zoomFactor = 11,
                country = country
            )
        )
        "GB" -> listOf(
            City(
                name = "London",
                stateCode = "ENG",
                latitude = 51.5074,
                longitude = -0.1278,
                zoomFactor = 11,
                country = country
            ),
            City(
                name = "Manchester",
                stateCode = "ENG",
                latitude = 53.4808,
                longitude = -2.2426,
                zoomFactor = 11,
                country = country
            )
        )
        else -> emptyList()
    }

    // Step 3: Persist parkings (requires persisted city)
    fun parkings(city: City) = when(city.name) {
        "New York" -> listOf(
            Parking(
                city = city,
                name = "Manhattan Central Garage",
                latitude = 40.7580f,
                longitude = -73.9855f,
                totalCapacity = 500,
                currentOccupied = 350,
                status = ParkingStatus.OPEN
            ),
            Parking(
                city = city,
                name = "Brooklyn Heights Parking",
                latitude = 40.6961f,
                longitude = -73.9969f,
                totalCapacity = 200,
                currentOccupied = 180,
                status = ParkingStatus.OPEN
            )
        )
        "Los Angeles" -> listOf(
            Parking(
                city = city,
                name = "Downtown LA Parking",
                latitude = 34.0407f,
                longitude = -118.2468f,
                totalCapacity = 800,
                currentOccupied = 450,
                status = ParkingStatus.OPEN
            ),
            Parking(
                city = city,
                name = "Hollywood Boulevard Garage",
                latitude = 34.1016f,
                longitude = -118.3267f,
                totalCapacity = 300,
                currentOccupied = 300,
                status = ParkingStatus.CLOSED
            )
        )
        "Paris" -> listOf(
            Parking(
                city = city,
                name = "Champs-Élysées Parking",
                latitude = 48.8698f,
                longitude = 2.3078f,
                totalCapacity = 450,
                currentOccupied = 420,
                status = ParkingStatus.OPEN
            ),
            Parking(
                city = city,
                name = "Louvre Underground",
                latitude = 48.8606f,
                longitude = 2.3376f,
                totalCapacity = 600,
                currentOccupied = 150,
                status = ParkingStatus.OPEN
            ),
            Parking(
                city = city,
                name = "Montmartre Garage",
                latitude = 48.8867f,
                longitude = 2.3431f,
                totalCapacity = 120,
                currentOccupied = 0,
                status = ParkingStatus.MAINTENANCE
            )
        )
        "Lyon" -> listOf(
            Parking(
                city = city,
                name = "Part-Dieu Station Parking",
                latitude = 45.7603f,
                longitude = 4.8592f,
                totalCapacity = 700,
                currentOccupied = 550,
                status = ParkingStatus.OPEN
            )
        )
        "Berlin" -> listOf(
            Parking(
                city = city,
                name = "Brandenburg Gate Parking",
                latitude = 52.5163f,
                longitude = 13.3777f,
                totalCapacity = 250,
                currentOccupied = 100,
                status = ParkingStatus.OPEN
            ),
            Parking(
                city = city,
                name = "Alexanderplatz Garage",
                latitude = 52.5219f,
                longitude = 13.4132f,
                totalCapacity = 400,
                currentOccupied = 380,
                status = ParkingStatus.OPEN
            )
        )
        "Munich" -> listOf(
            Parking(
                city = city,
                name = "Marienplatz Underground",
                latitude = 48.1374f,
                longitude = 11.5755f,
                totalCapacity = 550,
                currentOccupied = 275,
                status = ParkingStatus.OPEN
            ),
            Parking(
                city = city,
                name = "Olympiapark Parking",
                latitude = 48.1740f,
                longitude = 11.5516f,
                totalCapacity = 650,
                currentOccupied = 325,
                status = ParkingStatus.OPEN
            )
        )
        "Tokyo" -> listOf(
            Parking(
                city = city,
                name = "Shibuya Crossing Parking",
                latitude = 35.6595f,
                longitude = 139.7004f,
                totalCapacity = 350,
                currentOccupied = 340,
                status = ParkingStatus.OPEN
            ),
            Parking(
                city = city,
                name = "Shinjuku Station Garage",
                latitude = 35.6896f,
                longitude = 139.7006f,
                totalCapacity = 900,
                currentOccupied = 850,
                status = ParkingStatus.OPEN
            )
        )
        "Osaka" -> listOf(
            Parking(
                city = city,
                name = "Dotonbori Parking",
                latitude = 34.6686f,
                longitude = 135.5020f,
                totalCapacity = 180,
                currentOccupied = 90,
                status = ParkingStatus.OPEN
            ),
            Parking(
                city = city,
                name = "Umeda Sky Building",
                latitude = 34.7054f,
                longitude = 135.4903f,
                totalCapacity = 400,
                currentOccupied = 0,
                status = ParkingStatus.CLOSED
            )
        )
        "London" -> listOf(
            Parking(
                city = city,
                name = "Westminster Parking",
                latitude = 51.4994f,
                longitude = -0.1270f,
                totalCapacity = 300,
                currentOccupied = 250,
                status = ParkingStatus.OPEN
            ),
            Parking(
                city = city,
                name = "Tower Bridge Garage",
                latitude = 51.5055f,
                longitude = -0.0754f,
                totalCapacity = 220,
                currentOccupied = 22,
                status = ParkingStatus.OPEN
            )
        )
        "Manchester" -> listOf(
            Parking(
                city = city,
                name = "Piccadilly Gardens Parking",
                latitude = 53.4808f,
                longitude = -2.2370f,
                totalCapacity = 500,
                currentOccupied = 480,
                status = ParkingStatus.OPEN
            ),
            Parking(
                city = city,
                name = "Trafford Centre Parking",
                latitude = 53.4669f,
                longitude = -2.3521f,
                totalCapacity = 1000,
                currentOccupied = 750,
                status = ParkingStatus.OPEN
            )
        )
        else -> emptyList()
    }
}
