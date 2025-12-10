package com.duyguabbasoglu.hw2.api

import com.google.gson.annotations.SerializedName

class ApiResult {
    @SerializedName("books")
    var books: List<OnlineItem>? = null
}

class OnlineItem(
    @SerializedName("name") val name: String?,
    @SerializedName("author") val author: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("img") val img: String?
)