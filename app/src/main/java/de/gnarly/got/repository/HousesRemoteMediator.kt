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
class HousesRemoteMediator(
	private val gotClient: GoTClient,
	private val houseDao: HouseDao,
	private val housesPagingKeyStore: HousesPagingKeyStore
) : RemoteMediator<Int, HouseEntity>() {

	override suspend fun initialize(): InitializeAction {
		return if (housesPagingKeyStore.nextPage.first() == null) {
			InitializeAction.LAUNCH_INITIAL_REFRESH
		} else {
			InitializeAction.SKIP_INITIAL_REFRESH
		}
	}

	override suspend fun load(loadType: LoadType, state: PagingState<Int, HouseEntity>): MediatorResult {
		return try {
			when (loadType) {
				LoadType.REFRESH -> housesPagingKeyStore.resetNextPage()
				LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
				LoadType.APPEND -> {}
			}

			val nextPage = housesPagingKeyStore.nextPage.first() ?: return MediatorResult.Success(endOfPaginationReached = true)

			val pagedHousesResult = gotClient.getHousesPage(nextPage, state.config.pageSize)
			val pagedHouses = when (pagedHousesResult.isSuccess) {
				true -> pagedHousesResult.getOrThrow()
				false -> return MediatorResult.Error(pagedHousesResult.exceptionOrNull() ?: IllegalStateException("Something went wrong when loading page $nextPage"))
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