package com.example.weatherapppedrogarrido.repository

import androidx.compose.ui.text.intl.Locale
import com.example.weatherapppedrogarrido.data.remote.WeatherApi
import com.example.weatherapppedrogarrido.data.remote.response.Weather
import com.example.weatherapppedrogarrido.util.Constants
import com.example.weatherapppedrogarrido.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class WeatherRepository @Inject constructor(
    private val api: WeatherApi
) {
    suspend fun getCityWeather(
        cityName: String
    ): Resource<Weather> {
        val response = try {
            api.getCity(cityName, lang = Locale.current.language, units = "metric", app_id = Constants.API_KEY)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred!")
        }
        return Resource.Success(response)
    }
}