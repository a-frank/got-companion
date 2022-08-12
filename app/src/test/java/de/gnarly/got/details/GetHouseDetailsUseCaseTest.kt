package de.gnarly.got.details

import com.google.common.truth.Truth.assertThat
import de.gnarly.got.repository.GoTRepository
import de.gnarly.got.util.createHouse
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetHouseDetailsUseCaseTest {

	@Test
	fun `get house with lord name`(): Unit = runBlocking {
		val house = createHouse(1)
		val lord = "A lord"

		val firstExpected = house.copy(currentLord = "")
		val secondExpected = house.copy(currentLord = lord)


		val repo = mockk<GoTRepository> {
			every { getHouseFlow(eq(1)) } returns flowOf(house)
			every { getNameOfTheCurrentLord(any()) } returns flowOf("", lord)
		}

		val useCase = GetHouseDetailsUseCase(repo)
		val result = useCase(1).take(2).toList()

		assertThat(result).hasSize(2)
		assertThat(result[0]).isEqualTo(firstExpected)
		assertThat(result[1]).isEqualTo(secondExpected)
	}
}