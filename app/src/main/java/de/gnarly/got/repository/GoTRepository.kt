package de.gnarly.got.repository

import androidx.paging.*
import arrow.core.Either
import arrow.core.getOrHandle
import arrow.core.left
import arrow.core.right
import de.gnarly.got.database.CharacterDao
import de.gnarly.got.database.CharacterEntity
import de.gnarly.got.database.HouseDao
import de.gnarly.got.database.HouseEntity
import de.gnarly.got.model.House
import de.gnarly.got.network.CharacterDto
import de.gnarly.got.network.GoTClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class GoTRepository @Inject constructor(
	private val gotClient: GoTClient,
	private val houseDao: HouseDao,
	private val characterDao: CharacterDao,
	private val housesPagingKeyStore: HousesPagingKeyStore
) : CoroutineScope {
	@OptIn(ExperimentalPagingApi::class)
	fun houses(pageSize: Int = 20): Flow<PagingData<House>> =
		Pager(
			config = PagingConfig(pageSize),
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

	fun getNameOfTheCurrentLord(url: String): Flow<String> {
		if (url.isNotBlank()) {
			launch {
				gotClient.getCharacter(url).map {
					val entity = it.toEntity()
					characterDao.storeCharacter(entity)
				}
			}

		}
		return getIdFromCharacterUrl(url)
			.map { id ->
				characterDao.getCharacterById(id)
					.map { it?.name ?: "" }
			}
			.getOrHandle {
				flowOf("")
			}
	}

	override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()
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

private fun getIdFromCharacterUrl(url: String): Either<Exception, Long> = try {
	url.substringAfterLast("/").toLong().right()
} catch (e: Exception) {
	e.left()
}
