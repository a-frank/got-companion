package de.gnarly.got.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import de.gnarly.got.model.House
import kotlinx.coroutines.flow.flowOf

@Composable
fun HousesOverviewScreen(
	vm: HousesOverviewViewModel,
	onHouseClicked: (House) -> Unit
) {

	val state by vm.state.collectAsState(initial = HousesOverviewViewModel.INITIAL_STATE)
	val houses = state.houses.collectAsLazyPagingItems()

	HousesOverviewLayout(houses = houses) {
		onHouseClicked(it)
	}

}

@Composable
fun HousesOverviewLayout(houses: LazyPagingItems<House>, onHouseClicked: (House) -> Unit) {
	Column(modifier = Modifier.padding(16.dp)) {
		LazyColumn(modifier = Modifier.fillMaxSize()) {
			items(items = houses, key = { house -> house.id }) { house ->
				if (house != null) {
					HouseItem(
						house = house,
						modifier = Modifier
							.clickable { onHouseClicked(house) }
							.padding(top = 2.dp, bottom = 2.dp)
					)
				}
			}
		}
	}
}

@Preview
@Composable
fun HousesOverviewLayoutPreview() {
	val pagingData = PagingData.from(listOf(houseLannister, houseStark))
	val data = flowOf(pagingData)

	HousesOverviewLayout(data.collectAsLazyPagingItems()) {}
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HouseItem(house: House, modifier: Modifier = Modifier) {
	Card(
		modifier = modifier
			.fillMaxWidth()
	) {
		Column(Modifier.padding(8.dp)) {
			Text(text = house.name, style = MaterialTheme.typography.bodyMedium)
			Text(text = house.words, style = MaterialTheme.typography.bodySmall)
		}
	}
}

@Preview
@Composable
fun HouseItemPreview() {
	HouseItem(house = houseLannister)
}

private val houseLannister: House =
	House(
		id = 1,
		name = "Lannisters",
		region = "The Westerlands",
		coatOfArms = "A golden lion rampant on a crimson field",
		words = "Hear Me Roar!",
		currentLord = "Lord Tyrion Lannister"
	)

private val houseStark: House =
	House(
		id = 2,
		name = "Stark",
		region = "The North",
		coatOfArms = "A grey direwolf's head facing sinister on a white field over green",
		words = "Winter Is Coming",
		currentLord = "Queen Sansa Stark"
	)