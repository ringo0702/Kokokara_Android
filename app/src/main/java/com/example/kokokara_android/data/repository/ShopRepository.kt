package com.example.kokokara_android.data.repository

import com.example.kokokara_android.data.api.HotPepperApiClient
import com.example.kokokara_android.data.api.Results
import com.example.kokokara_android.data.model.Shop

class ShopRepository {

    private val api = HotPepperApiClient.service

    // 検索半径をAPIのrangeパラメータに変換
    private fun radiusToRange(radius: Int): Int = when (radius) {
        300 -> 1
        500 -> 2
        1000 -> 3
        2000 -> 4
        3000 -> 5
        else -> 3
    }

    suspend fun searchShops(
        apiKey: String,
        lat: Double,
        lng: Double,
        radius: Int,
        genreCodes: List<String>,
        page: Int = 1,
        countPerPage: Int = 15
    ): Results {
        val start = (page - 1) * countPerPage + 1
        val genre = genreCodes.joinToString(",")
        return api.searchShops(
            apiKey = apiKey,
            lat = lat,
            lng = lng,
            range = radiusToRange(radius),
            genre = genre,
            count = countPerPage,
            start = start
        ).results
    }
}