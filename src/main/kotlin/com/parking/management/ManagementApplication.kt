package com.parking.management

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class ParkingManagementApplication

fun main(args: Array<String>) {
	runApplication<ParkingManagementApplication>(*args)
}