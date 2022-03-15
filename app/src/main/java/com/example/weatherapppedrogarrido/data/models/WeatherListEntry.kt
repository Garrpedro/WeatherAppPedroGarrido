package com.example.weatherapppedrogarrido.data.models

import android.os.Parcel
import android.os.Parcelable

data class WeatherListEntry(
    val cityName: String?,
    val tempCurr: Double,
    val tempMin: Double,
    val tempMax: Double,
    val windSpeed: Double,
    val humidity: Int,
    val description: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cityName)
        parcel.writeDouble(tempCurr)
        parcel.writeDouble(tempMin)
        parcel.writeDouble(tempMax)
        parcel.writeDouble(windSpeed)
        parcel.writeInt(humidity)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherListEntry> {
        override fun createFromParcel(parcel: Parcel): WeatherListEntry {
            return WeatherListEntry(parcel)
        }

        override fun newArray(size: Int): Array<WeatherListEntry?> {
            return arrayOfNulls(size)
        }
    }
}