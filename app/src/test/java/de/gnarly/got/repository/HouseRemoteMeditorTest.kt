package de.gnarly.got.repository

import androidx.paging.*
import arrow.core.left
import arrow.core.right
import com.google.common.truth.Truth.assertThat
import de.gnarly.got.database.HouseDao
import de.gnarly.got.database.HouseEntity
import de.gnarly.got.network.GoTClient
import de.gnarly.got.network.PagedHouses
import de.gnarly.got.util.createHouseDto
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class HouseRemoteMeditorTest {
	@Test
	fun `refresh paged data with a next page`(): Unit = runBlocking {
		val pageSize = 10
		val page = 1

		val dtos = listOf(createHouseDto(1), createHouseDto(2))
		val client = mockk<GoTClient> {
			coEvery { getHousesPage(eq(page), eq(pageSize)) } returns PagedHouses(page, 2, dtos).right()
		}

		val storedHouses = mutableListOf<HouseEntity>()
		val dao = mockk<HouseDao> {
			coEvery { storeHouses(any()) } answers {
				val entities = firstArg<List<HouseEntity>>()
				storedHouses.addAll(entities)
			}
		}
		val keyStore = mockk<HousesPagingKeyStore> {
			every { nextPage } returns flowOf(page)
			coEvery { resetNextPage() } just runs
			coEvery { saveNextPage(any()) } just runs
		}

		val mediator = HousesRemoteMediator(client, dao, keyStore)

		val result = mediator.load(
			loadType = LoadType.REFRESH,
			state = PagingState(
				pages = listOf(),
				anchorPosition = null,
				config = PagingConfig(pageSize),
				leadingPlaceholderCount = pageSize
			)
		)

		assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
		val success = result as RemoteMediator.MediatorResult.Success
		assertThat(success.endOfPaginationReached).isFalse()

		assertThat(storedHouses).hasSize(dtos.size)
		assertThat(storedHouses[0].name).isEqualTo(dtos[0].name)
		assertThat(storedHouses[1].name).isEqualTo(dtos[1].name)
	}

	@Test
	fun `get last page`(): Unit = runBlocking {
		val pageSize = 10
		val page = 2

		val client = mockk<GoTClient> {
			coEvery { getHousesPage(eq(page), eq(pageSize)) } returns PagedHouses(page, null, listOf(createHouseDto(1), createHouseDto(2))).right()
		}
		val dao = mockk<HouseDao> {
			coEvery { storeHouses(any()) } just runs
		}
		val keyStore = mockk<HousesPagingKeyStore> {
			every { nextPage } returns flowOf(page)
			coEvery { saveNextPage(any()) } just runs
		}

		val mediator = HousesRemoteMediator(client, dao, keyStore)

		val result = mediator.load(
			loadType = LoadType.APPEND,
			state = PagingState(
				pages = listOf(),
				anchorPosition = null,
				config = PagingConfig(pageSize),
				leadingPlaceholderCount = pageSize
			)
		)

		assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
		val success = result as RemoteMediator.MediatorResult.Success
		assertThat(success.endOfPaginationReached).isTrue()
	}

	@Test
	fun `load page failed with failed reset`(): Unit = runBlocking {
		val exception = IllegalStateException("Did not work")
		val keyStore = mockk<HousesPagingKeyStore> {
			coEvery { resetNextPage() } just runs
			every { nextPage } returns flowOf(1)
		}
		val client = mockk<GoTClient> {
			coEvery { getHousesPage(any(), any()) } returns exception.left()
		}

		val mediator = HousesRemoteMediator(client, mockk(), keyStore)
		val result = mediator.load(
			loadType = LoadType.REFRESH,
			state = PagingState(
				pages = listOf(),
				anchorPosition = null,
				config = PagingConfig(1),
				leadingPlaceholderCount = 1
			)
		)
		assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Error::class.java)
		val error = result as RemoteMediator.MediatorResult.Error
		assertThat(error.throwable).isEqualTo(exception)
	}
}