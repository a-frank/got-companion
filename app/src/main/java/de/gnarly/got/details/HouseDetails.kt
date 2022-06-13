package de.gnarly.got.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.gnarly.got.R
import de.gnarly.got.model.House

@Composable
fun HouseDetailsScreen(viewModel: HouseDetailsViewModel) {
	val state by viewModel.state.collectAsState(initial = HouseDetailsViewModel.INITIAL_STATE)

	HouseDetailsLayout(house = state.house)
}

@Composable
private fun HouseDetailsLayout(house: House?) {
	if (house == null) {
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			CircularProgressIndicator()
			Text(text = stringResource(id = R.string.loading_house_details), style = MaterialTheme.typography.bodyLarge)
		}
	} else {
		Column(
			modifier = Modifier.padding(16.dp)
		) {
			Text(text = house.name, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.semantics {
				testTag = "house name"
			})
			if (house.words.isNotEmpty()) {
				Text(text = house.words, style = MaterialTheme.typography.bodyMedium)
			}
			Spacer(modifier = Modifier.padding(16.dp))
			SpacedNotEmptyLabeledText(label = stringResource(R.string.current_lord), text = house.currentLord)
			SpacedNotEmptyLabeledText(label = stringResource(R.string.coat_of_arms), text = house.coatOfArms)
			SpacedNotEmptyLabeledText(label = stringResource(R.string.region), text = house.region)
			if (house.seats.any { it.isNotBlank() }) {
				Column {
					Text(text = "${stringResource(R.string.seats)}:", style = MaterialTheme.typography.labelMedium)
					house.seats.forEach {
						Text(text = it, style = MaterialTheme.typography.bodyMedium)
					}
				}
			}
		}
	}
}

@Composable
private fun SpacedNotEmptyLabeledText(label: String, text: String, spacing: Dp = 8.dp) {
	if (text.isNotEmpty()) {
		LabeledText(label = label, text = text)
		Spacer(modifier = Modifier.padding(spacing))
	}
}

@Preview
@Composable
fun HouseDetailsLayoutPreviewNoHouse() {
	HouseDetailsLayout(house = null)
}

@Preview
@Composable
fun HouseDetailsLayoutPreview() {
	HouseDetailsLayout(house = houseLannister)
}

@Composable
private fun LabeledText(label: String, text: String) {
	Column {
		Text(text = "$label:", style = MaterialTheme.typography.labelMedium)
		Text(text = text, style = MaterialTheme.typography.bodyMedium)
	}
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