package com.example.weatherapppedrogarrido.data.remote

import com.example.weatherapppedrogarrido.data.remote.response.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather?")
    suspend fun getCity(
        @Query("q") q: String,
        @Query("lang") lang: String,
        @Query("units") units: String,
        @Query("appid") app_id: String
    ) : Weather
}