package com.example.kokokara_android.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kokokara_android.data.model.Shop
import com.example.kokokara_android.data.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResultListUiState(
    val shops: List<Shop> = emptyList(),
    val totalCount: Int = 0,
    val currentPage: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val totalPages: Int get() = if (totalCount == 0) 1 else (totalCount + 14) / 15
}

class ResultListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ResultListUiState())
    val uiState: StateFlow<ResultListUiState> = _uiState.asStateFlow()

    private val repository = ShopRepository()

    // 検索パラメータを保持
    private var currentLat: Double = 0.0
    private var currentLng: Double = 0.0
    private var currentRadius: Int = 1000
    private var currentGenres: List<String> = emptyList()

    fun search(
        apiKey: String,
        lat: Double,
        lng: Double,
        radius: Int,
        genreCodes: List<String>,
        page: Int = 1
    ) {
        currentLat = lat
        currentLng = lng
        currentRadius = radius
        currentGenres = genreCodes

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            try {
                val results = repository.searchShops(
                    apiKey = apiKey,
                    lat = lat,
                    lng = lng,
                    radius = radius,
                    genreCodes = genreCodes,
                    page = page
                )
                _uiState.value = _uiState.value.copy(
                    shops = results.shop,
                    totalCount = results.resultsAvailable,
                    currentPage = page,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "検索に失敗しました: ${e.message}"
                )
            }
        }
    }

    fun onNextPage(apiKey: String) {
        val state = _uiState.value
        if (state.currentPage < state.totalPages) {
            search(apiKey, currentLat, currentLng, currentRadius, currentGenres, state.currentPage + 1)
        }
    }

    fun onPrevPage(apiKey: String) {
        val state = _uiState.value
        if (state.currentPage > 1) {
            search(apiKey, currentLat, currentLng, currentRadius, currentGenres, state.currentPage - 1)
        }
    }

    // 店舗IDから店舗情報を取得
    fun getShopById(id: String): Shop? = _uiState.value.shops.find { it.id == id }
}