package de.gnarly.got.network

import kotlinx.serialization.Serializable

@Serializable
class HouseDto(
	val url: String,
	val name: String,
	val region: String,
	val coatOfArms: String,
	val words: String,
	val currentLord: String
)