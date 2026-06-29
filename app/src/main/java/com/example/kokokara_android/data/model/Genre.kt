package com.example.kokokara_android.data.model

data class Genre(
    val code: String,
    val name: String
)

val allGenres = listOf(
    Genre("G001", "居酒屋"),
    Genre("G002", "ダイニングバー・バル"),
    Genre("G003", "創作料理"),
    Genre("G004", "和食"),
    Genre("G005", "洋食"),
    Genre("G006", "イタリアン・フレンチ"),
    Genre("G007", "中華"),
    Genre("G008", "焼肉・ホルモン"),
    Genre("G009", "韓国料理"),
    Genre("G017", "アジア・エスニック料理"),
    Genre("G010", "各国料理"),
    Genre("G011", "カラオケ・パーティ"),
    Genre("G012", "バー・カクテル"),
    Genre("G013", "ラーメン"),
    Genre("G016", "お好み焼き・もんじゃ"),
    Genre("G014", "カフェ・スイーツ"),
    Genre("G015", "その他グルメ")
)