package de.gnarly.got.repository

import androidx.paging.*
import de.gnarly.got.database.CharacterDao
import de.gnarly.got.database.CharacterEntity
import de.gnarly.got.database.HouseDao
import de.gnarly.got.database.HouseEntity
import de.gnarly.got.model.House
import de.gnarly.got.network.CharacterDto
import de.gnarly.got.network.GoTClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoTRepository @Inject constructor(
	private val gotClient: GoTClient,
	private val houseDao: HouseDao,
	private val characterDao: CharacterDao,
	private val housesPagingKeyStore: HousesPagingKeyStore
) {
	@OptIn(ExperimentalPagingApi::class)
	fun houses(pageSize: Int = 20): Flow<PagingData<House>> =
		Pager(
			PagingConfig(pageSize),
			initialKey = 0,
			remoteMediator = HousesRemoteMediator(gotClient, houseDao, housesPagingKeyStore)
		) {
			houseDao.getHousesPaged()
		}
			.flow
			.map { pagingData ->
				pagingData
					.map {
						it.toHouse()
					}
			}
			.flowOn(Dispatchers.IO)

	fun getHouseFlow(id: Int): Flow<House> =
		houseDao.getHouseById(id)
			.filterNotNull()
			.map {
				it.toHouse()
			}

	suspend fun getNameOfTheCurrentLord(url: String): Flow<String?> {
		if (url.isNotBlank()) {
			coroutineScope {
				val character = gotClient.getCharacter(url)
				if (character != null) {
					val entity = character.toEntity()
					characterDao.storeCharacter(entity)
				}
			}
		}
		return characterDao.getCharacterByUrl(url)
			.map { it?.name }
	}
}

private fun HouseEntity.toHouse(): House =
	House(
		id = id,
		name = name,
		region = region,
		coatOfArms = coatOfArms,
		words = words,
		currentLord = currentLord,
		seats = seats
	)

private fun CharacterDto.toEntity(): CharacterEntity =
	CharacterEntity(
		id = url.substringAfterLast("/").toLong(),
		url = url,
		name = name
	)

