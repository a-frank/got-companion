package de.gnarly.got.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.gnarly.got.repository.GoTRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HouseDetailsViewModel @Inject constructor(
	gotRepository: GoTRepository,
	savedStateHandle: SavedStateHandle
) : ViewModel() {
	@OptIn(ExperimentalCoroutinesApi::class)
	val state: Flow<HouseDetailsViewState> = savedStateHandle
		.getLiveData<String>(paramId)
		.asFlow()
		.map {
			it.toIntOrNull()
		}
		.filterNotNull()
		.flatMapLatest {
			gotRepository.getHouseFlow(it)
		}
		.flatMapLatest { house ->
			gotRepository.getNameOfTheCurrentLord(house.currentLord)
				.map { lord ->
					house to lord
				}
		}
		.map { (house, lord) ->
			HouseDetailsViewState(
				house = house.copy(currentLord = lord ?: "")
			)
		}
		.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(5_000),
			INITIAL_STATE
		)

	companion object {
		val INITIAL_STATE = HouseDetailsViewState()
	}
}