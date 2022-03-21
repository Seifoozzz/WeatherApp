package com.example.weatherapp.models

import androidx.room.TypeConverters
import com.example.weatherapp.database.converters.WeatherConverter
import com.google.gson.annotations.SerializedName
@TypeConverters(WeatherConverter::class)
data class Current(

	@field:SerializedName("sunrise")
	val sunrise: Double,

	@field:SerializedName("temp")
	val temp: Double? = null,

	@field:SerializedName("visibility")
	val visibility: Double? = null,

	@field:SerializedName("uvi")
	val uvi: Double? = null,

	@field:SerializedName("pressure")
	val pressure: Double? = null,

	@field:SerializedName("clouds")
	val clouds: Double? = null,

	@field:SerializedName("feels_like")
	val feelsLike: Double? = null,

	@field:SerializedName("wind_gust")
	val windGust: Double? = null,

	@field:SerializedName("dt")
	val dt: Double? = null,

	@field:SerializedName("wind_deg")
	val windDeg: Double? = null,

	@field:SerializedName("dew_point")
	val dewPoint: Double? = null,

	@field:SerializedName("sunset")
	val sunset: Double? = null,

	@field:SerializedName("weather")
	val weather: List<WeatherItem?>? = null,

	@field:SerializedName("humidity")
	val humidity: Double? = null,

	@field:SerializedName("wind_speed")
	val windSpeed: Double? = null
)