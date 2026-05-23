package com.pokedex.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponseDto(
    val count: Int,
    val results: List<PokemonListItemDto>
)

@Serializable
data class PokemonListItemDto(
    val name: String,
    val url: String
)

@Serializable
data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonTypeSlotDto>,
    val stats: List<PokemonStatSlotDto>,
    val sprites: SpritesDto
)

@Serializable
data class PokemonTypeSlotDto(
    val slot: Int,
    val type: NamedResourceDto
)

@Serializable
data class PokemonStatSlotDto(
    @SerialName("base_stat") val baseStat: Int,
    val stat: NamedResourceDto
)

@Serializable
data class SpritesDto(
    val other: OtherSpritesDto? = null,
    @SerialName("front_default") val frontDefault: String? = null
)

@Serializable
data class OtherSpritesDto(
    @SerialName("official-artwork") val officialArtwork: OfficialArtworkDto? = null
)

@Serializable
data class OfficialArtworkDto(
    @SerialName("front_default") val frontDefault: String? = null
)

@Serializable
data class NamedResourceDto(
    val name: String,
    val url: String
)

@Serializable
data class PokemonSpeciesDto(
    @SerialName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntryDto>
)

@Serializable
data class FlavorTextEntryDto(
    @SerialName("flavor_text") val flavorText: String,
    val language: NamedResourceDto
)