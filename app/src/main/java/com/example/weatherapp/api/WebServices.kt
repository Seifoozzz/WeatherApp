package com.example.weatherapp.api


import com.example.weatherapp.BuildConfig
import com.example.weatherapp.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WebServices {
    @GET("/data/2.5/onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid")appid:String,
        @Query("units") units: String,
        @Query("lang") lang: String

    ): Response<WeatherResponse>
}