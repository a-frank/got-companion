package de.gnarly.got.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
	entities = [HouseEntity::class, CharacterEntity::class],
	version = 1
)
@TypeConverters(
	StringListConverter::class
)
abstract class GoTDatabase : RoomDatabase() {
	abstract fun getHouseDao(): HouseDao

	abstract fun getCharacterDao(): CharacterDao
}