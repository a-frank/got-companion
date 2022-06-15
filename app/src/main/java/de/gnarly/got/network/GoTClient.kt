package de.gnarly.got.network

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import de.gnarly.got.coroutines.Io
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoTClient @Inject constructor(
	private val httpClient: HttpClient,
	private val linkHandler: LinkHandler,
	@BaseUrl private val baseUrl: String,
	@Io private val coroutineDispatcher: CoroutineDispatcher
) {
	suspend fun getHousesPage(page: Int, pageSize: Int): Either<Exception, PagedHouses> = withContext(coroutineDispatcher) {
		try {
			val response = httpClient.get("$baseUrl/houses?page=$page&pageSize=$pageSize")
			if (response.status.value == HttpStatusCode.OK.value) {
				val housesDtos = response.body<List<HouseDto>>()
				val linkHeader = response.headers["Link"]
				val nextPage = linkHandler.getNextPage(linkHeader)
				PagedHouses(
					currentPage = page,
					nextPage = nextPage,
					houses = housesDtos
				).right()

			} else {
				IllegalStateException("Could not load page $page. Response code: ${response.status}").left()
			}
		} catch (e: Exception) {
			Timber.e(e)
			e.left()
		}
	}

	suspend fun getCharacter(url: String): Either<Exception, CharacterDto> = withContext(coroutineDispatcher) {
		try {
			val response = httpClient.get(url)
			if (response.status.value == HttpStatusCode.OK.value) {
				response.body<CharacterDto>().right()
			} else {
				IllegalStateException("Failed to load character with error code ${response.status.value}").left()
			}
		} catch (e: Exception) {
			Timber.e(e)
			e.left()
		}
	}
}