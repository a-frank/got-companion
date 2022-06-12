package de.gnarly.got.overview

import de.gnarly.got.model.House
import kotlinx.coroutines.runBlocking
import org.junit.Test

class HouseOverviewViewModelTest {
	@Test
	fun `get a paging source`(): Unit = runBlocking {

		// TODO
	}

	private fun createHouse(id: Int): House =
		House(id, "H$id", "R$id", "C$id", "W$id", "L$id", emptyList())
}