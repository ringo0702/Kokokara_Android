package com.example.kokokara_android.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kokokara_android.data.LatLng
import com.example.kokokara_android.data.LocationManager
import com.example.kokokara_android.data.model.Genre
import com.example.kokokara_android.data.model.allGenres
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

val radiusOptions = listOf(300, 500, 1000, 2000, 3000)

data class SearchUiState(
    val selectedRadius: Int = 1000,
    val selectedGenres: Set<String> = emptySet(),
    val currentLocation: LatLng? = null,
    val isLoadingLocation: Boolean = false,
    val locationError: String? = null
)

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    val genres: List<Genre> = allGenres

    private val locationManager = LocationManager(application)

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

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingLocation = true,
                locationError = null
            )
            try {
                val location = locationManager.getCurrentLocation()
                _uiState.value = _uiState.value.copy(
                    currentLocation = location,
                    isLoadingLocation = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingLocation = false,
                    locationError = "現在地を取得できませんでした"
                )
            }
        }
    }
}