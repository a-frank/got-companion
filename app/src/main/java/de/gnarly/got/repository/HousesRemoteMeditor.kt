package de.gnarly.got.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import de.gnarly.got.database.HouseDao
import de.gnarly.got.database.HouseEntity
import de.gnarly.got.network.GoTClient
import de.gnarly.got.network.HouseDto
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
class HousesRemoteMeditor(
	private val gotClient: GoTClient,
	private val houseDao: HouseDao,
	private val housesPagingKeyStore: HousesPagingKeyStore
) : RemoteMediator<Int, HouseEntity>() {

	override suspend fun load(loadType: LoadType, state: PagingState<Int, HouseEntity>): MediatorResult {
		return try {
			when (loadType) {
				LoadType.REFRESH ->
					housesPagingKeyStore.resetNextPage()

				LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
				LoadType.APPEND -> {}
			}

			val nextPage = housesPagingKeyStore.nextPage.first() ?: return MediatorResult.Success(endOfPaginationReached = true)

			val pagedHouses = gotClient.getHousesPage(nextPage, state.config.pageSize)

			if (loadType == LoadType.REFRESH) {
				houseDao.clearHouses()
			}

			val entities = pagedHouses.houses.map { house ->
				house.toHouseEntity()
			}
			houseDao.storeHouses(entities)
			housesPagingKeyStore.saveNextPage(pagedHouses.nextPage)

			MediatorResult.Success(endOfPaginationReached = pagedHouses.nextPage == null)
		} catch (e: Exception) {
			MediatorResult.Error(e)
		}
	}
}

private fun HouseDto.toHouseEntity(): HouseEntity =
	HouseEntity(
		id = url.substringAfterLast("/").toInt(),
		name = name,
		region = region,
		coatOfArms = coatOfArms,
		words = words,
		currentLord = currentLord,
		seats = seats
	)