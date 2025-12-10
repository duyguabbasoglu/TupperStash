package com.duyguabbasoglu.hw2.api

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("tuppers")
    val tupperList: List<ApiTupper>? = null
)

data class ApiTupper(
    @SerializedName("name") val name: String,
    @SerializedName("color") val color: String,
    @SerializedName("items") val items: List<ApiItem>
)

data class ApiItem(
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String
)