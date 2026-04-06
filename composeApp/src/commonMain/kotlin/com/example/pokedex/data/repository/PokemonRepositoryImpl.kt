package com.pokedex.app.data.repository

import com.pokedex.app.data.remote.PokeApiService
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.model.PokemonDetail
import com.pokedex.app.domain.model.PokemonStat
import com.pokedex.app.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PokemonRepositoryImpl(
    private val api: PokeApiService = PokeApiService()
) : PokemonRepository {


    private val detailCache = mutableMapOf<Int, PokemonDetail>()

    override suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon> {
        val listResponse = api.getPokemonList(limit, offset)


        return coroutineScope {
            listResponse.results
                .chunked(20)
                .flatMap { chunk ->
                    chunk.map { item ->
                        async {
                            val id = extractIdFromUrl(item.url)
                            val detail = api.getPokemonDetail(id)
                            Pokemon(
                                id = id,
                                name = item.name.formatPokemonName(),
                                imageUrl = buildImageUrl(id),
                                types = detail.types
                                    .sortedBy { it.slot }
                                    .map { it.type.name }
                            )
                        }
                    }.awaitAll()
                }
        }
    }

    override suspend fun getPokemonDetail(id: Int): PokemonDetail {
        detailCache[id]?.let { return it }

        val detail  = api.getPokemonDetail(id)
        val species = runCatching { api.getPokemonSpecies(id) }.getOrNull()

        val description = species?.flavorTextEntries
            ?.firstOrNull { it.language.name == "en" }
            ?.flavorText
            ?.cleanFlavorText()
            ?: "No description available."

        return PokemonDetail(
            id          = detail.id,
            name        = detail.name.formatPokemonName(),
            imageUrl    = buildImageUrl(detail.id),
            types       = detail.types.sortedBy { it.slot }.map { it.type.name },
            stats       = detail.stats.map { PokemonStat(it.stat.name, it.baseStat) },
            description = description,
            heightM     = detail.height / 10f,
            weightKg    = detail.weight / 10f
        ).also { detailCache[id] = it }
    }

    private fun extractIdFromUrl(url: String): Int =
        url.trimEnd('/').substringAfterLast('/').toInt()

    private fun buildImageUrl(id: Int): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

    private fun String.formatPokemonName(): String =
        split("-").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

    private fun String.cleanFlavorText(): String =
        replace("\n", " ").replace("\u000c", " ").trim()
}