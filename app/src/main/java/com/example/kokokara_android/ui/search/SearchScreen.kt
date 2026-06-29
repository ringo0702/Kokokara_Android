package com.example.kokokara_android.ui.search

import androidx.lifecycle.ViewModel
import com.example.kokokara_android.data.model.Genre
import com.example.kokokara_android.data.model.allGenres
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 検索半径の選択肢（メートル）
val radiusOptions = listOf(300, 500, 1000, 2000, 3000)

data class SearchUiState(
    val selectedRadius: Int = 1000,
    val selectedGenres: Set<String> = emptySet() // Genre.code のセット
)

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    val genres: List<Genre> = allGenres

    fun onRadiusSelected(radius: Int) {
        _uiState.value = _uiState.value.copy(selectedRadius = radius)
    }

    fun onGenreToggled(code: String) {
        val current = _uiState.value.selectedGenres.toMutableSet()
        if (current.contains(code)) current.remove(code) else current.add(code)
        _uiState.value = _uiState.value.copy(selectedGenres = current)
    }

    fun onClearGenres() {
        _uiState.value = _uiState.value.copy(selectedGenres = emptySet())
    }
}