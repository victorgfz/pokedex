package com.pokedex.app.domain.usecase

import com.pokedex.app.domain.model.PokemonDetail
import com.pokedex.app.domain.repository.PokemonRepository

class GetPokemonDetailUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(id: Int): PokemonDetail =
        repository.getPokemonDetail(id)
}