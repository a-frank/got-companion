package de.gnarly.got.util

import de.gnarly.got.model.House
import de.gnarly.got.network.HouseDto

fun createHouse(id: Int): House =
	House(
		id = id,
		name = "N$id",
		region = "R$id",
		coatOfArms = "C$id",
		words = "W$id",
		currentLord = "L$id",
		seats = listOf()
	)

fun createHouseDto(id: Int): HouseDto =
	HouseDto(
		url = "http://house/$id",
		name = "N$id",
		region = "R$id",
		coatOfArms = "C$id",
		words = "W$id",
		currentLord = "L$id",
		seats = listOf()
	)