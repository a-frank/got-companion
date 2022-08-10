package de.gnarly.got.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
	@Query("SELECT * FROM Character WHERE id = :id")
	fun getCharacterById(id: Long): Flow<CharacterEntity?>

	@Query("SELECT id not null FROM Character WHERE url = :url")
	fun doesCharacterExist(url: String): Boolean

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun storeCharacter(characterEntity: CharacterEntity)
}