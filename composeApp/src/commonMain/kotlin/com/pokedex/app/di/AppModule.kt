package com.pokedex.app.di

import com.pokedex.app.data.local.database.AppDatabase
import com.pokedex.app.data.local.database.getDatabase
import com.pokedex.app.data.local.database.getDatabaseBuilder
import com.pokedex.app.data.remote.PokeApiService
import com.pokedex.app.data.repository.PokemonRepositoryImpl
import com.pokedex.app.domain.repository.PokemonRepository
import com.pokedex.app.domain.usecase.GetPokemonDetailUseCase
import com.pokedex.app.domain.usecase.GetPokemonListUseCase
import com.pokedex.app.domain.usecase.SearchPokemonUseCase

object AppModule {
    
    private val database: AppDatabase by lazy {
        getDatabase(getDatabaseBuilder())
    }

    private val apiService: PokeApiService by lazy {
        PokeApiService()
    }

    val pokemonRepository: PokemonRepository by lazy {
        PokemonRepositoryImpl(
            api = apiService,
            dao = database.pokemonDao()
        )
    }

    val getPokemonList: GetPokemonListUseCase by lazy {
        GetPokemonListUseCase(pokemonRepository)
    }

    val getPokemonDetail: GetPokemonDetailUseCase by lazy {
        GetPokemonDetailUseCase(pokemonRepository)
    }

    val searchPokemon: SearchPokemonUseCase by lazy {
        SearchPokemonUseCase(pokemonRepository)
    }
}