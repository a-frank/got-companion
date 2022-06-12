package de.gnarly.got.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Character")
data class CharacterEntity(
	@PrimaryKey val id: Long,
	val url: String,
	val name: String
)