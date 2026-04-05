package com.pokedex.app.domain.usecase

import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.repository.PokemonRepository

class GetPokemonListUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(limit: Int = 151, offset: Int = 0): List<Pokemon> =
        repository.getPokemonList(limit, offset)
}