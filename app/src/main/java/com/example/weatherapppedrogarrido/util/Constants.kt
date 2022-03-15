package com.example.weatherapppedrogarrido.util

object Constants {

    const val BASE_URL = "https://api.openweathermap.org/"
    const val API_KEY = "ec437bec4245258abc4ae1c34ada4f06"

    enum class CityNames(val names: String) {
        LISBON("lisbon"),
        MADRID("madrid"),
        PARIS("paris"),
        BERLIN("berlin"),
        COPENHAGEN("copenhagen"),
        ROME("rome"),
        LONDON("london"),
        DUBLIN("dublin"),
        PRAGUE("prague"),
        VIENNA("vienna")
    }
}