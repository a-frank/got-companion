package de.gnarly.got.database

import androidx.room.TypeConverter

object StringListConverter {
	@TypeConverter
	fun fromListToString(list: List<String>): String {
		return list.joinToString(separator = ",")
	}

	@TypeConverter
	fun fromStringToList(value: String): List<String> {
		return value.split(",")
	}
}