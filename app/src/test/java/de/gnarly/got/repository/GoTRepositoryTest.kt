package de.gnarly.got.repository

import arrow.core.right
import com.google.common.truth.Truth.assertThat
import de.gnarly.got.database.CharacterDao
import de.gnarly.got.database.CharacterEntity
import de.gnarly.got.database.HouseDao
import de.gnarly.got.database.HouseEntity
import de.gnarly.got.network.CharacterDto
import de.gnarly.got.network.GoTClient
import io.mockk.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GoTRepositoryTest {
	@Test
	fun `getting house details flow`(): Unit = runBlocking {
		val entity = HouseEntity(
			id = 2,
			name = "House",
			region = "Here",
			coatOfArms = "White on White",
			words = "Test me",
			currentLord = "JUnit",
			seats = listOf("Git")
		)

		val houseDao = mockk<HouseDao> {
			every { getHouseById(eq(2)) } returns flowOf(entity)
		}

		val repo = repo(houseDao = houseDao)
		val houseFlow = repo.getHouseFlow(2)
		val house = houseFlow.first()
		assertThat(house.id).isEqualTo(entity.id)
		assertThat(house.name).isEqualTo(entity.name)
		assertThat(house.region).isEqualTo(entity.region)
		assertThat(house.coatOfArms).isEqualTo(entity.coatOfArms)
		assertThat(house.words).isEqualTo(entity.words)
		assertThat(house.currentLord).isEqualTo(entity.currentLord)
		assertThat(house.seats).isEqualTo(entity.seats)
	}

	@Test
	fun `get blank name of current lord`(): Unit = runBlocking {
		val characterDao = mockk<CharacterDao> {
			every { getCharacterById(any()) } returns emptyFlow()
		}
		val gotClient = mockk<GoTClient>()

		val repo = repo(characterDao = characterDao, gotClient = gotClient)
		val nameFlow = repo.getNameOfTheCurrentLord("")
		val names = nameFlow.toList()
		assertThat(names).hasSize(1)
		assertThat(names[0]).isEqualTo("")
	}

	@Test
	fun `get name of current lord`(): Unit = runBlocking {
		val entity = CharacterEntity(
			id = 5,
			url = "http/5",
			name = "Oh Lord"
		)
		val dto = CharacterDto(
			url = "http/5",
			name = "Oh Lord"
		)

		val characterDao = mockk<CharacterDao> {
			every { getCharacterById(any()) } returns flowOf(entity)
			coEvery { storeCharacter(any()) } just runs
			coEvery { doesCharacterExist(any()) } returns false
		}
		val gotClient = mockk<GoTClient> {
			coEvery { getCharacter(any()) } returns dto.right()
		}

		val repo = repo(characterDao = characterDao, gotClient = gotClient)
		val nameFlow = repo.getNameOfTheCurrentLord("http/5")
		val name = nameFlow.first()
		assertThat(name).isEqualTo(entity.name)
		coVerify(exactly = 1) {
			gotClient.getCharacter(any())
		}
		coVerify(exactly = 1) {
			characterDao.storeCharacter(any())
		}
	}

	private fun repo(
		gotClient: GoTClient = mockk(),
		houseDao: HouseDao = mockk(),
		characterDao: CharacterDao = mockk(),
		housesPagingKeyStore: HousesPagingKeyStore = mockk(),
		dispatcher: CoroutineDispatcher = Dispatchers.Default
	): GoTRepository =
		GoTRepository(
			gotClient = gotClient,
			houseDao = houseDao,
			characterDao = characterDao,
			housesPagingKeyStore = housesPagingKeyStore,
			coroutineDispatcher = dispatcher
		)
}