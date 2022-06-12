package de.gnarly.got.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HouseDao {
	@Query("SELECT * FROM House")
	fun getHousesPaged(): PagingSource<Int, HouseEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun storeHouses(entities: List<HouseEntity>)

	@Query("SELECT * FROM HOUSE WHERE id = :id")
	fun getHouseById(id: Int): Flow<HouseEntity>
}