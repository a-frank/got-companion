package de.gnarly.got.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import arrow.core.continuations.either
import arrow.core.getOrHandle
import de.gnarly.got.database.HouseDao
import de.gnarly.got.database.HouseEntity
import de.gnarly.got.network.GoTClient
import de.gnarly.got.network.HouseDto
import kotlinx.coroutines.flow.first
import timber.log.Timber

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

	override suspend fun load(loadType: LoadType, state: PagingState<Int, HouseEntity>): MediatorResult =
		either<Exception, MediatorResult> {
			when (loadType) {
				LoadType.REFRESH -> housesPagingKeyStore.resetNextPage()
				LoadType.PREPEND -> return@either MediatorResult.Success(endOfPaginationReached = true)
				LoadType.APPEND -> {}
			}

			val nextPage = housesPagingKeyStore.nextPage.first() ?: return@either MediatorResult.Success(endOfPaginationReached = true)

			val pagedHouses = gotClient.getHousesPage(nextPage, state.config.pageSize).bind()

			val entities = pagedHouses.houses.map { house ->
				house.toHouseEntity()
			}
			houseDao.storeHouses(entities)
			housesPagingKeyStore.saveNextPage(pagedHouses.nextPage)

			MediatorResult.Success(endOfPaginationReached = pagedHouses.nextPage == null)
		}.getOrHandle {
			Timber.e(it)
			MediatorResult.Error(it)
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