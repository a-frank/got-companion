package de.gnarly.got.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "House")
class HouseEntity(
	@PrimaryKey val id: Int = 0,
	val name: String,
	val region: String,
	val coatOfArms: String,
	val words: String,
	val currentLord: String,
	val seats: List<String>
)