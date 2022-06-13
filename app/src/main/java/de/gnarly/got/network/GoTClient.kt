package de.gnarly.got.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoTClient @Inject constructor(
	private val httpClient: HttpClient,
	private val linkHandler: LinkHandler,
	@BaseUrl private val baseUrl: String
) {
	suspend fun getHousesPage(page: Int, pageSize: Int): PagedHouses {
		val response = httpClient.get("$baseUrl/houses?page=$page&pageSize=$pageSize")
		return try {
			if (response.status.value == HttpStatusCode.OK.value) {
				val housesDtos = response.body<List<HouseDto>>()
				val linkHeader = response.headers["Link"]
				val nextPage = linkHandler.getNextPage(linkHeader)
				PagedHouses(
					currentPage = page,
					nextPage = nextPage,
					houses = housesDtos
				)
			} else {
				PagedHouses(
					currentPage = page,
					nextPage = null,
					houses = emptyList()
				)
			}
		} catch (e: Exception) {
			Timber.e(e)
			PagedHouses(
				currentPage = page,
				nextPage = null,
				houses = emptyList()
			)
		}
	}

	suspend fun getCharacter(url: String): CharacterDto? = try {
		val response = httpClient.get(url)
		if (response.status.value == HttpStatusCode.OK.value) {
			response.body()
		} else {
			null
		}
	} catch (e: Exception) {
		Timber.e(e)
		null
	}
}