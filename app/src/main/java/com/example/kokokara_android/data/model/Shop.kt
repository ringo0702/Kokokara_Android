package com.example.kokokara_android.data.model

import com.google.gson.annotations.SerializedName

data class Shop(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("catch") val catchCopy: String,
    @SerializedName("access") val access: String,
    @SerializedName("address") val address: String,
    @SerializedName("photo") val photo: Photo,
    @SerializedName("genre") val genre: GenreInfo,
    @SerializedName("budget") val budget: BudgetInfo,
    @SerializedName("budget_memo") val budgetMemo: String,
    @SerializedName("open") val open: String,
    @SerializedName("card") val card: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)

data class Photo(
    @SerializedName("pc") val pc: PhotoUrl
)

data class PhotoUrl(
    @SerializedName("l") val large: String,
    @SerializedName("m") val medium: String,
    @SerializedName("s") val small: String
)

data class GenreInfo(
    @SerializedName("name") val name: String
)

data class BudgetInfo(
    @SerializedName("average") val average: String
)