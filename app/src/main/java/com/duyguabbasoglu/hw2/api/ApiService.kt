package com.duyguabbasoglu.hw2.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("2dc0703aee01f2d559ef")
    fun getOnlineData(): Call<ApiResponse>
}