package de.gnarly.got.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.gnarly.got.repository.GoTRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@HiltViewModel
class HousesOverviewViewModel @Inject constructor(
	gotRepository: GoTRepository
) : ViewModel() {

	private val _state = MutableStateFlow(INITIAL_STATE)
	val state: Flow<HousesOverviewViewState> = _state

	init {
		_state.value = HousesOverviewViewState(
			houses = gotRepository.houses()
				.cachedIn(viewModelScope)
		)
	}

	companion object {
		val INITIAL_STATE = HousesOverviewViewState(emptyFlow())
	}
}