package de.gnarly.got.overview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import de.gnarly.got.R
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
	Box(
		modifier = Modifier
			.padding(16.dp)
			.fillMaxSize()
	) {
		LazyColumn(
			modifier = Modifier.fillMaxSize()
		) {
			items(
				items = houses,
				key = { house -> house.id }
			) { house ->
				if (house != null) {
					HouseItem(
						house = house,
						modifier = Modifier
							.fillMaxWidth()
							.clickable { onHouseClicked(house) }
							.padding(top = 2.dp, bottom = 2.dp)
					)
				}
			}
		}
		AnimatedVisibility(
			visible = houses.loadState.append is LoadState.Loading,
			modifier = Modifier.align(BottomCenter)
		) {
			CircularProgressIndicator()
		}
		AnimatedVisibility(
			visible = houses.loadState.append is LoadState.Error,
			modifier = Modifier
				.align(BottomEnd)
				.padding(16.dp)
		) {
			FloatingActionButton(onClick = { houses.retry() }) {
				Icon(
					imageVector = Icons.Default.Warning,
					contentDescription = stringResource(id = R.string.retry_loading)
				)
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

@Preview
@Composable
fun HousesOverviewLayoutLoadingPreview() {
	val pagingData = PagingData.empty<House>(
		LoadStates(
			refresh = LoadState.NotLoading(false),
			prepend = LoadState.NotLoading(false),
			append = LoadState.Loading
		)
	)
	val data = flowOf(pagingData)

	HousesOverviewLayout(data.collectAsLazyPagingItems()) {}
}

@Preview
@Composable
fun HousesOverviewLayoutRetryPreview() {
	val pagingData = PagingData.empty<House>(
		LoadStates(
			refresh = LoadState.NotLoading(false),
			prepend = LoadState.NotLoading(false),
			append = LoadState.Error(IllegalStateException("error loading"))
		)
	)
	val data = flowOf(pagingData)

	HousesOverviewLayout(data.collectAsLazyPagingItems()) {}
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HouseItem(house: House, modifier: Modifier = Modifier) {
	Card(
		modifier = modifier.semantics {
			testTag = "house ${house.id}"
		}
	) {
		Column(Modifier.padding(8.dp)) {
			Text(text = house.name, style = MaterialTheme.typography.bodyLarge)
			if (house.words.isNotEmpty()) {
				Text(text = house.words, style = MaterialTheme.typography.bodySmall)
			}
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
		currentLord = "Lord Tyrion Lannister",
		seats = listOf("Hand of the King")
	)

private val houseStark: House =
	House(
		id = 2,
		name = "Stark",
		region = "The North",
		coatOfArms = "A grey direwolf's head facing sinister on a white field over green",
		words = "Winter Is Coming",
		currentLord = "Queen Sansa Stark",
		seats = listOf("Wardens of the North")
	)