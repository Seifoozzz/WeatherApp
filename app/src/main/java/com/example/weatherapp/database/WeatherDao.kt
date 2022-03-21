package com.example.weatherapp.database

import androidx.room.*
import com.example.weatherapp.models.WeatherResponse


@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherResponse?)

    @Query("SELECT * FROM WeatherResponse WHERE lat = :lat and lon = :lon")
    suspend fun getCityData(lat: Double, lon: Double): WeatherResponse

    @Query("SELECT * from WeatherResponse WHERE isFavorite = 1")
    suspend fun getAllCities(): List<WeatherResponse>

    @Query("DELETE FROM WeatherResponse WHERE lat = :lat and lon = :lon")
    suspend fun deleteCityData(lat: Double, lon: Double)

    @Query("DELETE FROM WeatherResponse WHERE isFavorite = 0")
    suspend fun deleteOldCurrent()

    @Query("DELETE FROM WeatherResponse")
    suspend fun deleteAll()
}
