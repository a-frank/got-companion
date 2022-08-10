package de.gnarly.got.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class HouseDetailsViewModel @Inject constructor(
	getHouseDetailsUseCase: GetHouseDetailsUseCase,
	savedStateHandle: SavedStateHandle
) : ViewModel() {
	val state: Flow<HouseDetailsViewState> = savedStateHandle
		.getLiveData<String>(paramId)
		.asFlow()
		.map {
			it.toIntOrNull()
		}
		.filterNotNull()
		.flatMapLatest {
			getHouseDetailsUseCase(it)
		}
		.map { house ->
			HouseDetailsViewState(
				house = house
			)
		}

	companion object {
		val INITIAL_STATE = HouseDetailsViewState()
	}
}