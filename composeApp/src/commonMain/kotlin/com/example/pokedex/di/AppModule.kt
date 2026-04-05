package com.pokedex.app.di

import com.pokedex.app.data.repository.PokemonRepositoryImpl
import com.pokedex.app.domain.repository.PokemonRepository
import com.pokedex.app.domain.usecase.GetPokemonDetailUseCase
import com.pokedex.app.domain.usecase.GetPokemonListUseCase


object AppModule {
    val pokemonRepository: PokemonRepository by lazy { PokemonRepositoryImpl() }
    val getPokemonList: GetPokemonListUseCase by lazy { GetPokemonListUseCase(pokemonRepository) }
    val getPokemonDetail: GetPokemonDetailUseCase by lazy { GetPokemonDetailUseCase(pokemonRepository) }
}