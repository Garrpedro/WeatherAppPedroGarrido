package com.example.weatherapppedrogarrido.util

import android.os.Bundle
import androidx.navigation.NavType
import com.example.weatherapppedrogarrido.R
import com.example.weatherapppedrogarrido.data.models.WeatherListEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Utils {
    fun getImageDrawable(weatherDescription: String?): Int {
        return when (weatherDescription) {
            "clear sky" -> R.drawable.ic_clear_sky_day
            "few clouds" -> R.drawable.ic_few_clouds_day
            "scattered clouds" -> R.drawable.ic_scattered_clouds
            "broken clouds" -> R.drawable.ic_broken_clouds
            "shower rain" -> R.drawable.ic_shower_rain_day
            "rain" -> R.drawable.ic_rain_day
            "thunderstorm" -> R.drawable.ic_thunderstorm_day
            "snow" -> R.drawable.ic_snow_day
            "mist" -> R.drawable.ic_mist_day
            else -> R.drawable.ic_clear_sky_day
        }
    }

    object WeatherParamType : NavType<WeatherListEntry>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): WeatherListEntry? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): WeatherListEntry {
            return Gson().fromJson(value, WeatherListEntry::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: WeatherListEntry) {
            bundle.putParcelable(key, value)
        }
    }
}