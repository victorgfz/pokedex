package com.pokedex.app.presentation.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.app.di.AppModule
import com.pokedex.app.domain.model.PokemonDetail
import com.pokedex.app.domain.usecase.GetPokemonDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DetailUiState {
    data object Loading : DetailUiState()
    data class  Success(val pokemon: PokemonDetail) : DetailUiState()
    data class  Error(val message: String) : DetailUiState()
}

class DetailViewModel(
    private val getPokemonDetail: GetPokemonDetailUseCase = AppModule.getPokemonDetail
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun load(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            runCatching { getPokemonDetail(id) }
                .onSuccess { _uiState.value = DetailUiState.Success(it) }
                .onFailure { e -> _uiState.value = DetailUiState.Error(e.message ?: "Erro") }
        }
    }
}