package de.gnarly.got.overview

import androidx.paging.PagingData
import de.gnarly.got.model.House
import kotlinx.coroutines.flow.Flow

data class HousesOverviewViewState(
	val houses: Flow<PagingData<House>>
)