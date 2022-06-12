package de.gnarly.got.model

data class House(
	val id: Int,
	val name: String,
	val region: String,
	val coatOfArms: String,
	val words: String,
	val currentLord: String
)