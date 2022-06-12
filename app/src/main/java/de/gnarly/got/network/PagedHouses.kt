package de.gnarly.got.network

data class PagedHouses(
	val currentPage: Int,
	val nextPage: Int?,
	val houses: List<HouseDto>
)