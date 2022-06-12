package de.gnarly.got.network

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
	val url: String,
	val name: String
)
