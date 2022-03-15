package com.example.weatherapppedrogarrido.weatherlist

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.weatherapppedrogarrido.data.models.WeatherListEntry
import com.example.weatherapppedrogarrido.data.remote.response.Weather
import com.example.weatherapppedrogarrido.repository.WeatherRepository
import com.example.weatherapppedrogarrido.util.Constants
import com.example.weatherapppedrogarrido.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    applicationContext: Application,
    private val repository: WeatherRepository
) : ViewModel() {

    private var isFirstTime = true
    private var currentCity = 0
    private val localCity =
        applicationContext.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            .getString("localCity", "")

    var weatherList = mutableStateOf<List<WeatherListEntry?>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        loadWeatherCities()
    }

    fun loadWeatherCities() {
        viewModelScope.launch {
            isLoading.value = true
            val result: Resource<Weather> = if (isFirstTime && !localCity.isNullOrEmpty()) {
                repository.getCityWeather(localCity)
            } else {
                repository.getCityWeather(Constants.CityNames.values()[currentCity++].name)
            }
            when (result) {
                is Resource.Success -> {
                    if (isFirstTime) {
                        isFirstTime = false
                    }
                    endReached.value = currentCity >= Constants.CityNames.values().size
                    val weatherEntry =
                        result.data?.let {
                            WeatherListEntry(
                                it.name,
                                it.main.temp,
                                it.main.temp_min,
                                it.main.temp_max,
                                it.wind.speed,
                                it.main.humidity,
                                it.weather[0].description
                            )
                        }

                    loadError.value = ""
                    isLoading.value = false
                    weatherList.value += weatherEntry
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }

        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}