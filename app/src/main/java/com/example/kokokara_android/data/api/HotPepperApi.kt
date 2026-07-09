package com.example.kokokara_android.data.api

import com.example.kokokara_android.data.model.Shop
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// APIレスポンスのラッパー
data class HotPepperResponse(
    @SerializedName("results") val results: Results
)

data class Results(
    @SerializedName("shop") val shop: List<Shop> = emptyList(),
    @SerializedName("results_available") val resultsAvailable: Int = 0,
    @SerializedName("results_returned") val resultsReturned: Int = 0,
    @SerializedName("results_start") val resultsStart: Int = 0
)

interface HotPepperApiService {
    @GET("hotpepper/gourmet/v1/")
    suspend fun searchShops(
        @Query("key") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("range") range: Int,          // 1:300m 2:500m 3:1000m 4:2000m 5:3000m
        @Query("genre") genre: String = "",
        @Query("count") count: Int = 15,
        @Query("start") start: Int = 1,
        @Query("format") format: String = "json"
    ): HotPepperResponse
}

object HotPepperApiClient {
    private const val BASE_URL = "https://webservice.recruit.co.jp/"

    val service: HotPepperApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HotPepperApiService::class.java)
    }
}