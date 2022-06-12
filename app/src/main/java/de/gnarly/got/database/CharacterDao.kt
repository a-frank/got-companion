package de.gnarly.got.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
	@Query("SELECT * FROM CHARACTER WHERE url = :url")
	fun getCharacterByUrl(url: String): Flow<CharacterEntity?>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun storeCharacter(characterEntity: CharacterEntity)
}