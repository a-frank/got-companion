package de.gnarly.got.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun HouseDetails(viewModel: HouseDetailsViewModel) {
	val state by viewModel.state.collectAsState(initial = HouseDetailsViewModel.INITIAL_STATE)

	Text(text = "House ${state.house?.name}")
}