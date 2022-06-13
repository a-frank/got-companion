package de.gnarly.got.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LinkHandler @Inject constructor() {
	fun getNextPage(links: String?): Int? {
		val linkTypes = links?.split(",") ?: emptyList()
		val nextLink = linkTypes.find { it.contains("rel=\"next\"") }
		return nextLink?.substringAfter("page=")?.substringBefore("&")?.toIntOrNull()
	}
}