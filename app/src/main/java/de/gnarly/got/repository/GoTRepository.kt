package de.gnarly.got.repository

import androidx.paging.*
import de.gnarly.got.database.HouseDao
import de.gnarly.got.database.HouseEntity
import de.gnarly.got.model.House
import de.gnarly.got.network.GoTClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoTRepository @Inject constructor(
	private val gotClient: GoTClient,
	private val houseDao: HouseDao,
	private val housesPagingKeyStore: HousesPagingKeyStore
) {
	@OptIn(ExperimentalPagingApi::class)
	fun houses(pageSize: Int = 20): Flow<PagingData<House>> =
		Pager(
			PagingConfig(pageSize),
			initialKey = 1,
			remoteMediator = HousesRemoteMeditor(gotClient, houseDao, housesPagingKeyStore)
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

	fun getHouseFlow(id: Int): Flow<House> =
		houseDao.getHouseById(id)
			.filterNotNull()
			.map {
				it.toHouse()
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
