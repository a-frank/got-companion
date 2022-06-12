package de.gnarly.got.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
	entities = [HouseEntity::class],
	version = 1
)
abstract class GoTDatabase : RoomDatabase() {
	abstract fun getHouseDao(): HouseDao
}