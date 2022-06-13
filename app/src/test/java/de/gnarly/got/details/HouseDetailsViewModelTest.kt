package de.gnarly.got.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import de.gnarly.got.repository.GoTRepository
import de.gnarly.got.util.createHouse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class HouseDetailsViewModelTest {

	private val testScope = TestScope()
	private val testDispatcher = StandardTestDispatcher(testScope.testScheduler)

	@get:Rule
	var rule: TestRule = InstantTaskExecutorRule()

	@Before
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `loading details`(): Unit = testScope.runTest {

		val house = createHouse(1)
		val currentLord = "The current lord"
		val expected = house.copy(currentLord = currentLord)

		val repo = mockk<GoTRepository> {
			every { getHouseFlow(eq(1)) } returns flowOf(house)
			coEvery { getNameOfTheCurrentLord(any()) } returns flowOf(currentLord)

		}

		val savedState = SavedStateHandle()
		savedState.set(paramId, "1")
		val viewModel = HouseDetailsViewModel(repo, savedState)
		val state = viewModel.state.first()

		assertThat(state.house).isNotNull()
		assertThat(state.house).isEqualTo(expected)
	}
}