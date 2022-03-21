package com.example.weatherapp.models
import androidx.annotation.Nullable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.weatherapp.database.converters.AlertConverter
import com.example.weatherapp.database.converters.DailyConverter
import com.example.weatherapp.database.converters.HourlyConverter
import com.google.gson.annotations.SerializedName
@Entity(primaryKeys = ["lat", "lon"])
@TypeConverters(DailyConverter::class, HourlyConverter::class,AlertConverter::class)
data class WeatherResponse(
	@field:SerializedName("current")
	@Embedded(prefix = "curr_")
	val current: Current? = null,

	@field:SerializedName("timezone")
	val timezone: String,

	@field:SerializedName("timezone_offset")
	val timezoneOffset: Double,

	@field:SerializedName("daily")
	val daily: List<DailyItem?>? = null,

	@field:SerializedName("lon")
	val lon: Double,

	@field:SerializedName("hourly")
	val hourly: List<HourlyItem?>? = null,

	val alerts: List<Alerts>?,

	@field:SerializedName("lat")
	val lat: Double,
	var isFavorite: Boolean = false
) {

}