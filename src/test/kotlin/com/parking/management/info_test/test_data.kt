package com.parking.management.info_test

import com.parking.management.features.city.City
import com.parking.management.features.country.Country
import com.parking.management.features.parking.Parking
import com.parking.management.features.parking.ParkingStatus

object GeoFixtures {

    fun country(
        name: String = "Test Country",
        isoCode: String = "TC",
        latitude: Double = 0.0,
        longitude: Double = 0.0,
        zoomFactor: Int = 5
    ): Country =
        Country(
            name = name,
            isoCode = isoCode,
            latitude = latitude,
            longitude = longitude,
            zoomFactor = zoomFactor
        )

    fun city(
        name: String = "Test City",
        stateCode: String = "TS",
        latitude: Double = 0.0,
        longitude: Double = 0.0,
        zoomFactor: Int = 10,
        country: Country = country()
    ): City =
        City(
            name = name,
            stateCode = stateCode,
            latitude = latitude,
            longitude = longitude,
            zoomFactor = zoomFactor,
            country = country
        )

    fun parking(
        city: City = city(),
        name: String = "Test Parking",
        latitude: Float = 0f,
        longitude: Float = 0f,
        totalCapacity: Int = 100,
        currentOccupied: Int = 0,
        status: ParkingStatus = ParkingStatus.OPEN
    ): Parking =
        Parking(
            city = city,
            name = name,
            latitude = latitude,
            longitude = longitude,
            totalCapacity = totalCapacity,
            currentOccupied = currentOccupied,
            status = status
        )
}


object GeoFixtureSets {

    fun countries(): List<Country> = listOf(
        GeoFixtures.country(name = "France", isoCode = "FR"),
        GeoFixtures.country(name = "Germany", isoCode = "DE"),
        GeoFixtures.country(name = "Italy", isoCode = "IT")
    )

    fun cities(): List<City> {
        val countries = countries()
        return listOf(
            GeoFixtures.city(name = "Paris", stateCode = "PA", country = countries[0]),
            GeoFixtures.city(name = "Berlin", stateCode = "BE", country = countries[1]),
            GeoFixtures.city(name = "Rome", stateCode = "RM", country = countries[2])
        )
    }

    fun parkings(): List<Parking> {
        val cities = cities()

        return listOf(
            GeoFixtures.parking(
                city = cities[0],
                name = "Central Paris",
                currentOccupied = 20,
                totalCapacity = 70
            ),
            GeoFixtures.parking(
                city = cities[1],
                name = "Berlin Garage",
                currentOccupied = 50,
                status = ParkingStatus.OPEN,
                totalCapacity = 50
            ),
            GeoFixtures.parking(
                city = cities[2],
                name = "Rome Parking",
                currentOccupied = 5,
                totalCapacity = 20
            ),
            GeoFixtures.parking(
                city = cities[2],
                name = "Cavalin Parking",
                currentOccupied = 100
        )
        )
    }
}
